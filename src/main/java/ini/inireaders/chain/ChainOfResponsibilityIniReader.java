package ini.inireaders.chain;

import ini.IIniConfig;
import ini.InvalidConfigFileFormatException;
import ini.inireaders.IIniReader;
import ini.inireaders.chain.processors.ContextState;
import ini.inireaders.chain.processors.IContextState;
import ini.inireaders.chain.processors.ProcessorChain;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * For the detailed description of configuration file see:
 * {@link IIniReader}
 *
 * <p>
 * This is the concrete implementation of the above interface. It uses Chain-Of-Responsibility
 * design pattern to avoid if-else statements. The input line is passed to chain of
 * processors (implementation of AbstractProcessor). If the processor recognize the line,
 * it processes/swallows the line, otherwise it passes the line to the next Processor in the chain.
 * <p>
 * The stateful logic is still spread across all the methods...
 */
public class ChainOfResponsibilityIniReader implements IIniReader {

    private final ProcessorChain processorChain = new ProcessorChain();

    @Override
    public void load(@NotNull IIniConfig ini, @NotNull File file)
            throws InvalidConfigFileFormatException, IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            load(ini, reader);
        }

    }

    @Override
    public void load(@NotNull IIniConfig ini, @NotNull BufferedReader reader)
            throws IOException, InvalidConfigFileFormatException {

        IContextState contextState = new ContextState(ini);

        String line;
        while ((line = reader.readLine()) != null) {
            processorChain.processLine(contextState, line);
            if (contextState.isError()) {
                throw new InvalidConfigFileFormatException(contextState.getErrorMessage());
            }
        }
    }

}
