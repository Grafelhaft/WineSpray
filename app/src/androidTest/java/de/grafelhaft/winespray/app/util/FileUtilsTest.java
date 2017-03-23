package de.grafelhaft.winespray.app.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by @author Markus Graf (Grafelhaft) on 16.10.2016.
 */
public class FileUtilsTest {

    private static final String CONTENT = "Hello World!";

    @Test
    public void ioFile() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        FileUtils.writeFile(FileUtils.PATH_IMPORT, "test.txt", CONTENT);
        String in = FileUtils.readFile(FileUtils.PATH_IMPORT + "test.txt");

        assertEquals(true, in.equals(CONTENT));
    }

}