package ini.inireaders.chain;

import ini.inireaders.FactoryReader;
import ini.inireaders.IIniReaderTest;

public class ChainOfResponsibilityLoaderTest extends IIniReaderTest {

    public ChainOfResponsibilityLoaderTest() {
        iniReader = FactoryReader.getIniReader(FactoryReader.IniReaders.Chain);
    }

}
