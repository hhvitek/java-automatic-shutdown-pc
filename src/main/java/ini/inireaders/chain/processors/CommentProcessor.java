package ini.inireaders.chain.processors;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentProcessor extends AbstractProcessor {

    private final Pattern pattern = Pattern.compile("#\\s*(.*)");

    public CommentProcessor(AbstractProcessor nextProcessor) {
        super(nextProcessor);
    }

    @Override
    public void processLine(IContextState context, String line) {
        Matcher m = pattern.matcher(line);
        if (m.matches()) {
            String comment = m.group(1);
            context.addCommentLine(comment);
        } else {
            nextProcessor.processLine(context, line);
        }

    }

}
