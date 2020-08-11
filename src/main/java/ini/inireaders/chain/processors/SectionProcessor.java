package ini.inireaders.chain.processors;


import ini.inireaders.chain.IContextState;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SectionProcessor extends AbstractProcessor {

    private final Pattern pattern = Pattern.compile("\\[([a-zA-Z_0-9]+)]");

    public SectionProcessor(AbstractProcessor nextProcessor) {
        super(nextProcessor);
    }

    @Override
    public void processLine(IContextState context, String line) {
        Matcher m = pattern.matcher(line);
        if (m.matches()) {
            String sectionName = m.group(1);
            context.setActualSection(sectionName);
        } else {
            nextProcessor.processLine(context, line);
        }

    }

}
