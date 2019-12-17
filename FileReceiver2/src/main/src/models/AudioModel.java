package models;

import java.io.File;
import java.io.Serializable;

public class AudioModel implements Serializable {
    private File audio;
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public File getAudio() {
        return audio;
    }

    public Long getId() {
        return id;
    }

    public void setAudio(File audio) {
        this.audio = audio;
    }
}
