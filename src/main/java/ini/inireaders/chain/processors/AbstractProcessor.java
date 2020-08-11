package ini.inireaders.chain.processors;

import ini.inireaders.chain.IContextState;

public abstract class AbstractProcessor {

    protected final AbstractProcessor nextProcessor;

    protected AbstractProcessor(AbstractProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    public abstract void processLine(IContextState context, String line);

}
