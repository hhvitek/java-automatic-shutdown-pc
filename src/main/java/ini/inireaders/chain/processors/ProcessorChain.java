package ini.inireaders.chain.processors;

public class ProcessorChain {

    // the last in the chain is InvalidStateProcessor(null)
    // indicating the line wasn't recognized by any Processor
    private final AbstractProcessor chainOfProcessors = new BlankProcessor(new CommentProcessor(
            new SectionProcessor(new ItemProcessor(new InvalidStateProcessor(null)))));

    public void processLine(IContextState state, String line) {

        if (line == null) {
            state.setErrorMessage("There is null value as the line parameter.");
        } else {
            chainOfProcessors.processLine(state, line);
        }
    }

}
