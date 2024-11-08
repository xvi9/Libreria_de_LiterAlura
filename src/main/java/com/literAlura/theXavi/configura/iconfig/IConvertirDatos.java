package com.literAlura.theXavi.configura.iconfig;

public interface IConvertirDatos {

//    este metodo toma a json y convierte en datos Java ya que no se
//    sabe que se retornara
    <T> T convertirDatosJsonAJava(String json , Class<T> clase);
}
