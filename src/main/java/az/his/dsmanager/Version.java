package az.his.dsmanager;

class Version implements Comparable<Version>{
    private String name;
    private int id;
    private String fileName;

    Version(String name, int id, String fileName) {
        this.name = name;
        this.id = id;
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public int compareTo(Version other) {
        return Integer.compare(id, other.getId());
    }
}
