package de.stvehb.bazel.java.maven;

import de.stvehb.bazel.core.api.Project;
import de.stvehb.bazel.core.api.files.BazelPackage;
import de.stvehb.bazel.core.api.files.BazelWorkspace;
import de.stvehb.bazel.core.api.rule.ImportRule;
import de.stvehb.bazel.core.api.rule.Rule;
import de.stvehb.bazel.core.api.value.ArrayValue;
import de.stvehb.bazel.core.api.value.Glob;
import de.stvehb.bazel.core.api.value.StringValue;
import de.stvehb.bazel.core.util.AnalysisUtil;
import de.stvehb.bazel.core.util.FileUtil;
import de.stvehb.bazel.java.JavaLibrary;
import de.stvehb.bazel.java.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MavenProjectAnalyzer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MavenProjectAnalyzer.class.getSimpleName());

	private final Project project;
	private final BazelWorkspace workspace;
	private final List<BazelPackage> bazelPackages;

	private final ArrayValue<StringValue> installArtifacts = new ArrayValue<>();

	public MavenProjectAnalyzer(Project project) {
		this.project = project;
		this.workspace = project.getWorkspace();
		this.bazelPackages = project.getPackages();
	}

	/**
	 * Analyzes the project in given <i>projectDirectory</i>.
	 */
	@SuppressWarnings("UnusedLabel")
	public void analyzeProject(Path projectDirectory) {
		MavenDefaultSetup.setup(this.workspace, this.installArtifacts);

		try {
			Files.walk(projectDirectory)
				.filter(Files::isRegularFile)
				.filter(file -> file.getFileName().toString().equals("pom.xml"))
				.forEach(this::analyzePom);
		} catch (IOException ex) {
			LOGGER.error("Recursive analysis failed", ex);
			return;
		}

		cleanup: {
			// remove internal artifacts from the maven install
			this.installArtifacts.getValues().removeIf(
				artifact -> AnalysisUtil.isInternalTarget(
					this.project,
					AnalysisUtil.convertToTarget(artifact.getValue())
				)
			);
		}

		transform: {
			this.bazelPackages.forEach(bazelPackage -> {
				bazelPackage.getRules().stream().filter(r -> r instanceof JavaLibrary).map(r -> (JavaLibrary) r)
					.forEach(javaLibrary -> {
						javaLibrary.resolveDependencies(this.project);
					});
			});
		}
	}

	/**
	 * Analyzes the pom with given <i>pomPath</i>.
	 */
	@SuppressWarnings("UnusedLabel")
	public void analyzePom(Path pomPath) {
		try {
			Document doc = XmlUtil.parseDoc(pomPath);
			if (doc == null) throw new RuntimeException(); // just to shutdown the program

			Node projectNode = doc.getElementsByTagName("project").item(0);

			// general
			List<ImportRule> importRules = new ArrayList<>();
			List<Rule> rules = new ArrayList<>();
			BazelPackage bazelPackage = new BazelPackage(pomPath.getParent(), this.workspace, importRules, rules);
			JavaLibrary library;

			// dependency management
			ArrayValue<StringValue> libraryDependencies = new ArrayValue<>(); // dependencies of the current library

			elementaryInfo: { // Read elementary information about the module: groupId, artifactId and version
				Node parentNode = XmlUtil.getElement(projectNode, "parent");
				// depends on whether the current pom is the root pom or a child
				Node projectInfoNode = parentNode == null ? projectNode : parentNode;

				String projectGroupId = XmlUtil.getElement(projectInfoNode, "groupId").getTextContent();
				String projectVersion = XmlUtil.getElement(projectInfoNode, "version").getTextContent();
				String projectArtifactId = XmlUtil.getElement(projectNode, "artifactId").getTextContent();

				library = new JavaLibrary(projectGroupId + "_" + projectArtifactId, bazelPackage);// TODO: Method
				library.srcs(new Glob("src/main/java/**/*.java"));
				library.resources(new Glob("src/main/resources/**/*"));
			}

			dependencies: { // Read all dependencies of the current module including internal and external ones
				Node dependenciesNode = XmlUtil.getElement(projectNode, "dependencies");
				if (dependenciesNode == null) break dependencies;

				List<Node> dependencyNodes = XmlUtil.getChildren(dependenciesNode);
				dependencyNodes.forEach(dependency -> {
						String dependencyGroupId = XmlUtil.getElement(dependency, "groupId").getTextContent();
						String dependencyArtifactId = XmlUtil.getElement(dependency, "artifactId").getTextContent();
						String dependencyVersion = XmlUtil.getElement(dependency, "version").getTextContent();
						String dependencyScope = XmlUtil.getElement(dependency, "version").getTextContent();
						//TODO: scopes -> runtime deps, deps, test...

						libraryDependencies.addValues(
							new StringValue(
								":" + AnalysisUtil.convertToTarget(dependencyGroupId)
									+ "_"
									+ AnalysisUtil.convertToTarget(dependencyArtifactId)) //TODO: Method
						);

						String mavenDependency = dependencyGroupId + ":" + dependencyArtifactId + ":" + dependencyVersion; //TODO: Method
						this.installArtifacts.addValues(new StringValue(mavenDependency));
					}
				);

				// Add the dependencies to the current library
				for (StringValue dep : libraryDependencies.getValues()) library.deps(dep);
			}

			register: {// Register everything (more like: tie everything together)
				bazelPackage.getRules().add(library);
				this.project.getPackages().add(bazelPackage);
			}

		} catch (Exception ex) {
			LOGGER.error("Analysis of {} failed", pomPath, ex);
		}
	}

	/* Just for test purposes - use the CLI!*/
	public static void main(String... args) {
		// prepare test environment
		File testEnv = new File("test/java/");
		File tmpTestEnv = new File(MessageFormat.format("test/java-{0,number,#}/", System.currentTimeMillis()));
		FileUtil.copyDirectory(testEnv.toPath(), tmpTestEnv.toPath());

		long start = System.currentTimeMillis();
		try {
			Project project = new Project(tmpTestEnv.toPath());
			new MavenProjectAnalyzer(project).analyzeProject(tmpTestEnv.toPath());
			project.build();

			long time = System.currentTimeMillis() - start;
			LOGGER.info("Analyzed project in: {}ms", time);
		} finally {
			// cleanup test environment
			FileUtil.deleteDirectory(tmpTestEnv.toPath());
		}
	}

}
