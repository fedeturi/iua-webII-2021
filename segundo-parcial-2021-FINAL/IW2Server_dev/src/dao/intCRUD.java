/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;

/**
 *
 * @author csimes
 */
public interface intCRUD<EntidadDTO> {
    public boolean create(EntidadDTO e);

    public boolean delete(Object clave);

    public boolean update(EntidadDTO e);

    public EntidadDTO read(Object clave);

    public List<EntidadDTO> readAll();

}
