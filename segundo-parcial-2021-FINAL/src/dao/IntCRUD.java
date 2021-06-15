package dao;

import java.util.List;

public interface IntCRUD<EntidadDTO> {
    public boolean create(EntidadDTO e);

    public boolean delete(Object clave);

    public boolean update(EntidadDTO e);

    public EntidadDTO read(Object clave);

    public List<EntidadDTO> readAll();

}
