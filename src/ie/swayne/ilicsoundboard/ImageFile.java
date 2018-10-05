package ie.swayne.ilicsoundboard;

public class ImageFile {
    private String name;
    private String path;

    public ImageFile(String name, String path) {
        //get rid of jpg ending so it doesn't show up in name
       // int index = name.indexOf(".");
        //name = name.substring(0,index);
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }


    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String toString() {
        return this.name;
    }

}
