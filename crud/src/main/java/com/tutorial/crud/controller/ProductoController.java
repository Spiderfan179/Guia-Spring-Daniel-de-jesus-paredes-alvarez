package com.tutorial.crud.controller;

import com.tutorial.crud.entity.Producto;
import com.tutorial.crud.security.dto.ProductoDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.tutorial.crud.dto.Mensaje;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.crud.service.ProductoService;

import java.util.List;



import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductoController 
{
    @Autowired
    ProductoService productoService;

    @GetMapping("")
    public ResponseEntity<List<Producto>> findAll()
    {
        List<Producto> list = productoService.list();
        return new ResponseEntity<List<Producto>>(list,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id)
    {
        if(!productoService.existById(id))
            return new ResponseEntity<>(new Mensaje("El producto solicitado no existe"), HttpStatus.NOT_FOUND);
            Producto producto = productoService.getOne(id).get();
            return new ResponseEntity<Producto>(producto,HttpStatus.OK);
        
    }

  @GetMapping("/detail-name/{name}")
    public ResponseEntity<?> getByNombre(@PathVariable("name") String nombre)
    {
        if(!productoService.existByNombre(nombre))
            return new ResponseEntity<Mensaje>(new Mensaje("El producto con nombre "+ nombre + "no existe"), HttpStatus.NOT_FOUND);
            Producto producto = productoService.getByNombre(nombre).get();
            return new ResponseEntity<Producto>(producto,HttpStatus.OK);
        
    }
 @PreAuthorize("hasRole('ADMIN')") 
    @PostMapping("")
    public ResponseEntity<Mensaje> create(@RequestBody ProductoDto productoDto)
    {
        if(StringUtils.isBlank(productoDto.getNombre()))
        return new ResponseEntity<Mensaje>(new Mensaje("El nombre del producto es obligatorio"),HttpStatus.BAD_REQUEST);
        if(productoDto.getPrecio()==null|| productoDto.getPrecio()<0)
        return new ResponseEntity<Mensaje>(new Mensaje("El Precio debe ser mayor que 0.0"),HttpStatus.BAD_REQUEST);
        if(productoService.existByNombre(productoDto.getNombre()))
        return new ResponseEntity<Mensaje>(new Mensaje("El nombre " + productoDto.getNombre() + "ya se encuentra registrado"),HttpStatus.BAD_REQUEST);
        Producto producto = new Producto(productoDto.getNombre(),productoDto.getPrecio());
        productoService.save(producto);
        return new ResponseEntity<Mensaje>(new Mensaje("Producto creado con exito"),HttpStatus.CREATED);
    }

     @PreAuthorize("hasRole('ADMIN')")
     @PutMapping("/{id}")
    public ResponseEntity<Mensaje> update(@PathVariable("id") int id , @RequestBody ProductoDto productoDto)
    {   

        if(!productoService.existById(id))
        return new ResponseEntity<Mensaje>(new Mensaje("El Producto no existe"),HttpStatus.NOT_FOUND);
        if(productoService.existByNombre(productoDto.getNombre())&& productoService.getByNombre(productoDto.getNombre()).get().getId() != id)
        return new ResponseEntity<Mensaje>(new Mensaje("El nombre " + productoDto.getNombre() + "ya se encuentra registrado"),HttpStatus.BAD_REQUEST);
       if(StringUtils.isBlank(productoDto.getNombre()))
        return new ResponseEntity<Mensaje>(new Mensaje("El Nombre del producto es obligatorio"),HttpStatus.BAD_REQUEST);
        if(productoDto.getPrecio()==null|| productoDto.getPrecio()<0)
        return new ResponseEntity<Mensaje>(new Mensaje("El Precio debe ser mayor que 0.0"),HttpStatus.BAD_REQUEST);
       

        Producto producto = productoService.getOne(id).get();
        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        productoService.save(producto);
        return new ResponseEntity<Mensaje>(new Mensaje("Producto Actualizado con exito"),HttpStatus.CREATED);
    }
   
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Mensaje> delete(@PathVariable("id") int id)
    {
       if(!productoService.existById(id))
       return new ResponseEntity<Mensaje>(new Mensaje("El producto a eliminar no existe"),HttpStatus.NOT_FOUND);
       productoService.delete(id);
       return  new ResponseEntity<Mensaje>(new Mensaje("El producto eliminado"),HttpStatus.OK);
    }


    
}
