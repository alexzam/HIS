package az.his.clientdto;

import az.his.persist.TransactionCategory;

public class CategoryDto {
    private final int id;
    private final String name;
    private final String type;

    public CategoryDto(TransactionCategory cat) {
        id = cat.getId();
        name = cat.getName();
        switch (cat.getType()) {
            case EXP:
                type = "e";
                break;
            case INC:
                type = "i";
                break;
            case NONE:
                type = "n";
                break;
            default:
                type = "null";
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
