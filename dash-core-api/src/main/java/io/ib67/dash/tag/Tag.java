package io.ib67.dash.tag;

public interface Tag {
    static Tag of(String identifier){
        return new SimpleTag(identifier);
    }
    String identifier();
}
