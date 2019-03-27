import com.mobiquityinc.packer.FileUtil;
import com.mobiquityinc.packer.exception.APIException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import static org.junit.Assert.*;

public class FileUtilTest {

    String fileName;

    @Before
    public void before() {

        File resourcesDirectory = new File("src/test/resources");
        fileName = resourcesDirectory.getAbsolutePath() + "/dummy_file.txt";
    }

    @Test
    public void testGetTextFromFile_NonExistingFile(){

        try {

            FileUtil.getTextFromFile("non_existent_file.txt");
            fail("APIException should have been thrown here");

        } catch (APIException e) {
          //--
        }
    }

    @Test
    public void testGetTextFromFile_ExistingFile(){

        try {

            String text = FileUtil.getTextFromFile(fileName);
            assertEquals("dummy\ntext", text);

        } catch (APIException e) {
            //shouldn't come here - means File not found
            fail();
        }
    }
}
