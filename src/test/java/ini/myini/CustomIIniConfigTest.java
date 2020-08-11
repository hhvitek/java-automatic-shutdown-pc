package ini.myini;


import ini.IIniConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class CustomIIniConfigTest {

    @Test
    void downloadPhotos_configFileDoesNOTExists_throwsIOException() {

        String doesNotExistPath = "/ahoj/svete";

        IIniConfig ini = new CustomIIniConfig();

        Assertions.assertThrows(IOException.class, () -> ini.load(new File(doesNotExistPath)));
    }

}
