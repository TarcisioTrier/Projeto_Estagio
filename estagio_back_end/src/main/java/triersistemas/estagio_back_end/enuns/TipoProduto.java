package triersistemas.estagio_back_end.enuns;

public enum TipoProduto {

    ANTICONCEPCIONAIS,
    FITOTERAPICO,
    FRALDAS,
    GENERICO,
    HOMEOPATICO,
    LEITES,
    MANIPULADO,
    MEDICAMENTO,
    NAO_DEFINIDO,
    OFICINAL,
    PERFUMARIA,
    REFERENCIA,
    SIMILAR;
    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
