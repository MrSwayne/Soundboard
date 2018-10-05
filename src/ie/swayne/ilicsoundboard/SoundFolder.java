package ie.swayne.ilicsoundboard;

import java.util.ArrayList;

public class SoundFolder {

    private String folderName;
    private ArrayList<SoundFile> sounds;


    public SoundFolder(String folderName) {
        this.folderName = folderName;
        sounds = new ArrayList<SoundFile>();
    }

    public void add(SoundFile soundFile) {
        sounds.add(soundFile);
    }

    public String getFolderName() {
        return folderName;
    }

    public SoundFile get(int i) {
        if(i >= 0 && i < sounds.size())
            return sounds.get(i);
        return null;
    }

    public int size() {
        return sounds.size();
    }

    public String toString() {
        String line = "";

        for(int i = 0;i < sounds.size();i++) {
            line += sounds.get(i).toString() + " ";
        }

        return this.folderName + "//" + line;
    }
}
