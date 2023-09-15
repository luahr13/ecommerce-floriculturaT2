package br.luahr.topicos1.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoFlor {
    ROSA(1, "Rosa"),
    CRAVO(2, "Cravo"),
    TULIPA(3, "Tulipa"),
    MARGARIDA(4, "Margarida"),
    GIRASSOL(5, "Girassol"),
    ORQUIDEA(6, "Orquidea");

    private int id;
    private String label;

    private TipoFlor(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }

    public static TipoFlor valueOf(Integer id) throws IllegalArgumentException {
        if (id == null)
            return null;
        for(TipoFlor tipoFlor : TipoFlor.values()) {
            if (id.equals(tipoFlor.getId()))
                return tipoFlor;
        } 
        throw new IllegalArgumentException("Id inválido:" + id);
    }
}
