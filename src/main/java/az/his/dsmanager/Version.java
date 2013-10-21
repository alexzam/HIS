package az.his.dsmanager;

class Version implements Comparable<Version>{
    private String name;
    private int id;
    private String fileName;
    private String dumpName;

    Version(String name, int id, String fileName, String dumpName) {
        this.name = name;
        this.id = id;
        this.fileName = fileName;
        this.dumpName = dumpName;
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

    String getDumpName() {
        return dumpName;
    }

    @Override
    public int compareTo(Version other) {
        return Integer.compare(id, other.getId());
    }
}
