package ini.inireaders.chain.processors;


public class InvalidStateProcessor extends AbstractProcessor {

    public InvalidStateProcessor(AbstractProcessor nextProcessor) {
        super(null);
    }

    @Override
    public void processLine(IContextState context, String line) {
        context.setErrorMessage("Unknown line format: " + line);
    }

}
