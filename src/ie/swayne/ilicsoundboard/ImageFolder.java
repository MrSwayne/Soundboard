package ie.swayne.ilicsoundboard;

import java.util.ArrayList;

public class ImageFolder {

    private String folderName;
    private ArrayList<ImageFile> images;

    public ImageFolder(String folderName) {
        this.folderName = folderName;
        images = new ArrayList<ImageFile>();

    }

    public void add(ImageFile imageFile) {
        images.add(imageFile);
    }

    public String getFolderName() {
        return folderName;
    }

    public ImageFile get(int i) {
        if(i >= 0 && i < images.size())
            return images.get(i);
        return null;
    }

    public int size() {
        return images.size();
    }

    public String toString() {
        String line = "";

        for(int i = 0;i < images.size();i++) {
            line += images.get(i).toString() + " ";
        }

        return this.folderName + "//" + line;
    }

}
