package ini.inireaders.chain.processors;


import ini.inireaders.chain.IContextState;

public class BlankProcessor extends AbstractProcessor {

    /**
     * If this is the first time there has ever been any blank/empty line
     * in the configuration file. There is the possibility that everything parsed so far
     * had actually been the header comment:
     * <p>
     * # header comment the first line
     * # header comment the second line
     * empty line
     * # section comment
     * [section1]
     * item_key = item_value
     */
    private boolean firstTime = true;

    public BlankProcessor(AbstractProcessor nextProcessor) {
        super(nextProcessor);
    }

    @Override
    public void processLine(IContextState context, String line) {
        if (line != null && line.isBlank()) {
            if (firstTime) {
                context.setHeaderComment();
                firstTime = false;
            }
        } else {
            nextProcessor.processLine(context, line);
        }
    }

}
