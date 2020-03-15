package de.stvehb.bazel.cli;

import com.beust.jcommander.JCommander;
import de.stvehb.bazel.cli.command.MigrateJavaCommand;
import de.stvehb.bazel.cli.command.HelpCommand;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaticBazelCLI {

	private static final List<CommandContainer<?>> COMMAND_CONTAINERS = new ArrayList<>();

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void main(String... args) {
		COMMAND_CONTAINERS.add(new CommandContainer<>("help", HelpCommand.class, HelpCommand::handle, "usage", "usages"));
		COMMAND_CONTAINERS.add(new CommandContainer<>("migrate:java", MigrateJavaCommand.class, MigrateJavaCommand::handle));

		JCommander.Builder builder = JCommander.newBuilder().acceptUnknownOptions(true).programName("StaticBazel");
		for (CommandContainer<?> commandContainer : COMMAND_CONTAINERS)
			builder.addCommand(commandContainer.getCommand(), commandContainer.getArgs(), commandContainer.aliases);

		Options options = new Options();
		JCommander commander = builder.addObject(options).build();
		commander.parse(args);

		String cmd = commander.getParsedCommand();
		Optional<CommandContainer<?>> optionalContainer = COMMAND_CONTAINERS.stream().filter(c -> c.getCommand().equalsIgnoreCase(cmd)).findAny();

		if (optionalContainer.isPresent()) {
			CommandContainer commandContainer = optionalContainer.get();
			commandContainer.getConsumer().accept(commander, options, commandContainer.getArgs());
		} else {
			commander.usage();
		}
	}

	@Getter
	public static class CommandContainer<T> {
		private String command;
		private String[] aliases;
		private Class<T> clazz;
		private T args;
		private TriConsumer<JCommander, Options, T> consumer;

		@SneakyThrows
		public CommandContainer(String command, Class<T> clazz, TriConsumer<JCommander, Options, T> consumer, String... aliases) {
			this.command = command;
			this.clazz = clazz;
			this.consumer = consumer;
			this.aliases = aliases;
			this.args = clazz.newInstance();
		}

	}

	@FunctionalInterface
	public interface TriConsumer<F, S, T> {
		void accept(F f, S s, T t);
	}

}
