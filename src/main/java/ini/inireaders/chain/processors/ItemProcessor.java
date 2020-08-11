package ini.inireaders.chain.processors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemProcessor extends AbstractProcessor {

    private final Pattern pattern = Pattern.compile("([a-zA-Z_0-9]+)\\s*=\\s*(.*)");

    public ItemProcessor(AbstractProcessor nextProcessor) {
        super(nextProcessor);
    }

    @Override
    public void processLine(IContextState context, String line) {
        Matcher m = pattern.matcher(line);
        if (m.matches()) {
            String key = m.group(1);
            String value = m.group(2);

            context.setItem(key, value);
        } else {
            nextProcessor.processLine(context, line);
        }

    }

}
