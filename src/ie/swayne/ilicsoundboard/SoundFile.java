package ie.swayne.ilicsoundboard;

class SoundFile {

    private String name;
    private String path;

    public SoundFile(String name, String path) {
        //get rid of mp3 ending so it doesn't show up in button name
        int index = name.indexOf(".");
        name = name.substring(0,index);
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
