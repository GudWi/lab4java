package convert;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@Default
@Dependent
public class AudioConverter {
    public File ConvertFileToAIFF(File inputFile) {
        AudioFileFormat inFileFormat;
        File inFile;
        File outFile;
        try {
            inFile = inputFile;
            outFile = File.createTempFile("audio", ".aiff");
        } catch (NullPointerException | IOException ex) {
            System.out.println("Error: one of the ConvertFileToAIFF parameters is null!");
            return null;
        }
        try {
            if (!inFile.canRead()) {
                inFile.setReadable(true);
            }
            // query file type
            inFileFormat = AudioSystem.getAudioFileFormat(inFile);
            if (inFileFormat.getType() != AudioFileFormat.Type.AIFF)
            {
                // inFile is not AIFF, so let's try to convert it.
                AudioInputStream inFileAIS =
                        AudioSystem.getAudioInputStream(inFile);
                //inFileAIS.reset(); // rewind
                if (AudioSystem.isFileTypeSupported(
                        AudioFileFormat.Type.AIFF, inFileAIS)) {
                    // inFileAIS can be converted to AIFF.
                    // so write the AudioInputStream to the
                    // output file.
                    AudioSystem.write(inFileAIS,
                            AudioFileFormat.Type.AIFF, outFile);
                    System.out.println("Successfully made AIFF file, "
                            + outFile.getPath() + ", from "
                            + inFileFormat.getType() + " file, " +
                            inFile.getPath() + ".");
                    inFileAIS.close();
                    return outFile; // All done now
                } else
                    System.out.println("Warning: AIFF conversion of "
                            + inFile.getPath()
                            + " is not currently supported by AudioSystem.");
            } else
                System.out.println("Input file " + inFile.getPath() +
                        " is AIFF." + " Conversion is unnecessary.");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Error: " + inFile.getPath()
                    + " is not a supported audio file type!");
            return null;
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Error: failure attempting to read "
                    + inFile.getPath() + "!");
            return null;
        }

        return outFile;
    }
}
