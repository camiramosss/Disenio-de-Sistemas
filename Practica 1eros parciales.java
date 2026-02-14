// PRACTICA PARCIALES DISEÑO DE SISTEMAS (1ER PARCIAL)

//-------------------------------------MACOWINS-------------------------------------
// --- precio de venta de una prenda --> segun estado de prenda (nueva, promocion, liquidacion) + precio base
// --- tipo de prenda --> sacos, pantalones, camisas
// --- registro de ventas --> prendas vendidas, cantidad prendas vendidas, fecha de venta
// --- tipo de pago --> efectivo, tarjeta
// --- ganancias de un determinado dia

// Prendas

import java.util.List;

private class Prenda {
    private int precioBase;
    private Estado estado;
    private TipoPrenda tipoPrenda; // no hace falta hacer subclases (no tiene comportamiento distinto) --> ENUM
    private int precio() {
        return estado.precioFinal(precioBase);
    }
}

public enum TipoPrenda {
    SACO,
    PANTALON,
    CAMISA
}

interface Estado { // --> STRATEGY METHOD
    abstract Double precioFinal(Double precioBase);
}

private class Nueva implements Estado {
    public Double precioFinal(Double precioBase) {
        return precioBase; // no modifica el precioBase
    }
}

private class Promocion implements Estado {
    private int valorArestar; // valor decidido por el user
    public Double precioFinal(Double precioBase) {
        return precioBase - valorArestar;
    }
}

private class Liquidacion implements Estado {
    public Double precioFinal(Double precioBase) {
        return precioBase * 0.5; // 50% de descuento
    }
}

// Ventas
public class Venta{
    private List<Item> item; // list porque puede importar el orden (ya que tienen fecha)
    private Date fechaDeVenta;
    private TipoPago tipoPago;
    private int precioBaseVenta = items.forEach(item -> item.importe()).sum();
    private precioVenta(){
        tipoPago.precioSegunMedioDePago(precioBaseVenta)
    }
}

public class Item {
    private Prenda prenda;
    private int cantidad;

    public Double importe() {
        return prenda.precio() * cantidad;
    }
}

// para el tipo de pago se podria hacer:
// - strategy (hice)
// - template method por el metodo recargo() (la clase abstracta Venta define precioDeVenta() y usa recargo() dentro, el cual es definido por sus sublcases) !!!
// - 2 clases Venta y VentaConTarjeta (ya que no hay diferencia entre venta en efectivo y venta normal)

public class Efectivo inherits TipoPago {
    public int precioSegunMedioDePago(precioBaseVenta) {
        return precioBaseVenta
    }
}

public class Tarjeta inherits TipoPago {
    private int cantidadCuotas;
    private int coeficienteFijo;
    public int precioSegunMedioDePago(precioBaseVenta) {
        return cantidadCuotas * coeficienteFijo + precioBaseVenta * 0,01;
    }
}

// Ganancias
public class Ganancia {
    private List<Venta> ventas;
    public int gananciaDeUnDia(Date dia) { // no agrego el dia como atributo porque es solo para el metodo (se podrían agregar otros metodos como gananciaDeUnMes, gananciaDeUnAño, etc)
        return ventas.filter(venta ->venta.fechaDeVenta.equals(dia)) // no se usa el "== dia", se usa EQUALS
                     .forEach(venta -> venta.precioVenta())
                     .sum();
    }
}



//-------------------------------------QueMePongo1-------------------------------------
// --- Como usuario de QuéMePongo, quiero poder cargar prendas válidas para generar atuendos con ellas

// PRENDA VALIDA
// valida: que no le falte ningun atributo obligatorio
public class Prenda{
    private TipoPrenda tipoPrenda;
    private Material material;
    private Color colorPrincipal;
    private Color colorSecundario; // opcional --> usa otro constructor

    // contructor : expone los atributos indispensables para crear una prenda valida --> los suponemos por encima de setter
    public Prenda(TipoPrenda tipoPrenda, Material material, Color colorPrincipal) {
     this.tipoDePrenda = tipoDePrenda;
     this.material = material;
     this.colorPrincipal = color;
  }

    // constructor sobrecargado para color secundario
    public Prenda(TipoPrenda tipoPrenda, Material material, Color colorPrincipal, Color colorSecundario) {
     this.tipoDePrenda = tipoDePrenda;
     this.material = material;
     this.colorPrincipal = colorPrincipal;
     this.colorSecundario = colorSecundario;
  }

  // para saber la categoria, se lo delega al tipo
    public Categoria categoriaDeLaPrenda() {
        return tipoPrenda.categoria();
    }
}

// TIPOS DE PRENDA Y CATEGORIAS
public abstract class tipoDePrenda {
    private Categoria categoria; // asegura que no haya tipo sin categoria
    
    public Categoria categoria() {
        return this.categoria;
    }
}

public enum Categoria{
    PARTE_SUPERIOR,
    PARTE_INFERIOR,
    CALZADO,
    ACCESORIO
}

// MATERIALES Y COLORES
public enum Material {
    ALGODON,
    LANA,
    JEAN,
    CUERO,
    SEDA
}

// el color NO DEBERIA ser un enum, ya que hay muchos colores posibles (un user puede escribir algo como amarillo patito, naranja tigre, etc)
// podemos basarnos en cierto listado como HTML, o una paleta de 8, 16 o 20 colores
// O podemos armar una paleta RGB --> da mas libertad para elegir un color
class Color{
    int rojo;
    int verde;
    int azul;
}

// VALIDACIONES
// todos los parámetros recibidos deben ser no nulos --> FAIL FAST = en el contructor se fdlla lo antes posible
    public Prenda(TipoDePrenda tipo, Material material, Color color)
        this.tipoDePrenda = requireNonNull(tipo, "tipo de prenda es obligatorio")
        this.material = requireNonNull(material, "material es obligatorio")
        this.color = requireNonNull(color, "color es obligatorio")

        // similar a hacer --> pero arriba fallamos mas rapido y no necesitamos crear una excepcion por cada atributo
        if (color == null)
        throw new FaltaColorExcepcion();



//-------------------------------------QueMePongo2-------------------------------------
// --- saber la TRAMA de la TELA de la prenda (lisa, rayada, cuadriculada, estampada)
// --- crear prenda especificando en el siguiente orden: 1) tipo 2) material
// --- guardar un borrador de la ultima prenda
// --- si no se aclara la trama, se asume lisa
// --- guardar solo las prendas validas


// TRAMA
// debo agregar otro atributo en la clase Prenda, para la trama
public enum Trama {
    LISA, RAYADA, CUADRICULADA, ESTAMPADA
}

// MATERIAL
// sigue siendo un enum, no puede ser una clase con atributo trama porque:
// - cada vez que creo una prenda, debo instanciar un material nuevo (si tengo miles de prendas --> tengo miles de materiales en memoria)
// - voy a tener instancias distintas para el mismo material
// - pierdo seguridad de tipos del enum --> un user podria crear ALGDN en lugar de ALGODON

// --> a veces modelar la realidad tal cual nos trae problemas técnicos
public enum Material {
    ALGODON, LANA, JEAN, CUERO, SEDA
}

// ORDEN
// ya se asegura con el constructor
// aca tambien metemos que el material como default es liso (no hay otro lugar donde meterlo)
public Prenda(TipoPrenda tipoPrenda, Material material, Color colorPrincipal, Color colorSecundario) {
     this.tipoDePrenda = tipoDePrenda;
     this.material = material;
     this.trama = Trama.LISA; // default
     this.colorPrincipal = color;
     this.colorSecundario = colorSecundario;
}

// BORRADOR
// no podemos hacer que un borrador sea una PRENDA, ya que esta clase es INMUTABLE (se completan todos sus campos o no es valida)
// no hicimos mal en hacer la prenda inmutable, solo debemos crear otra clase para el borrador
// para el BORRADOR, necesitamos una clase mutable que guarde la prenda en construccion (de a pasos) --> BUILDER PATTERN
// el BUILDER tambien sirve al tener objetos con varios parametros opcionales y aquellos que necesitan extensibilidad futura
public class Borrador{
    // es igual a la prenda, pero sus campos NO SON OBLIGATORIOS
    private TipoPrenda tipoPrenda;
    private Material material;
    private Trama trama = Trama.LISA; // default
    private Color colorPrincipal;
    private Color colorSecundario;

    // SETTERS
    // son para ir completando el borrador --> importante en builder!! hace que sus atributos no sean obligatorios
    public void setTipoPrenda(TipoPrenda tipoPrenda) {
        this.tipoPrenda = requireNonNull(tipoPrenda, "tipo de prenda es obligatorio"); // ahora sí, CUANDO SE DECIDE agregar el tipo de prenda, no se le puede pasar null
    }
    public void setMaterial(Material material) {
        this.material = requireNonNull(material, "material es obligatorio");
    }
    public void setTrama(Trama trama) {
        if (trama != null) {
            this.trama = trama; // si no me pasan trama, queda la default (lisa)
        }
    }
    public void setColorPrincipal(Color colorPrincipal) {
        this.colorPrincipal = requireNonNull(colorPrincipal, "color principal es obligatorio");
    }
    public void setColorSecundario(Color colorSecundario){
        this.colorSecundario = colorSecundario; // puede ser null, no es obligatorio
    }

    // BUILD
    // buildeamos la prenda --> validamos que todos los campos estén llenos --> guardo solo prendas VALIDAS
    // es necesario validar aca tambien, ya que el user podría nunca llamar a los setters (se podría validar sólo aca, pero los setters aseguran el fail fast)
    // PD: el NullPointerException detiene el programa y muestra el mensaje
    public Prenda buildPrenda() {
        if (this.tipoPrenda == null) {
            throw new NullPointerException("No se puede crear la prenda: falta el tipo de prenda");
        }
        if (this.material == null) {
            throw new NullPointerException("No se puede crear la prenda: falta el material");
        }
        if (this.trama == null) {
            
        }
        if (this.colorPrincipal == null) {
            throw new NullPointerException("No se puede crear la prenda: falta el color principal");
        }

        // pasó las validaciones --> creo la prenda
        if (this.colorSecundario != null) { // este puede ser null --> lo acomodamos a los 2 constructores de Prenda
            return new Prenda(this.tipoPrenda, this.material, this.colorPrincipal, this.colorSecundario);
        } else {
            return new Prenda(this.tipoPrenda, this.material, this.colorPrincipal);
        }
    }
}

// PD!! si solo creamos una prenda a través del borrador --> NO HACE FALTA validar nulls en la prenda como antes, el constructor queda mas simple

// USUARIO
// es quien tiene el borrador, quien lo crea y lo modifica
public class Usuario {
    private Borrador borradorActual;

    // para crear un borrador, arranco siempre con el tipo de la prenda
    public void crearNuevoBorrador(TipoPrenda tipoPrenda) {
        this.borradorActual = new Borrador();
        this.borradorActual.setTipoPrenda(tipoPrenda);
    }

    // luego para agregar otros atributos, uso los setters del borradorActual
    // en cada uno antes de pedirle al borrador que se agregue el atributo, se podría validar que exista un borradorActual --> opcional
    public void agregarMaterialAlBorrador(Material material) {
        this.borradorActual.setMaterial(material);
    }
    public void agregarTramaAlBorrador(Trama trama) {
        this.borradorActual.setTrama(trama);
    }
    public void agregarColorPrincipalAlBorrador(Color colorPrincipal) {
        this.borradorActual.setColorPrincipal(colorPrincipal);
    }
    public void agregarColorSecundarioAlBorrador(Color colorSecundario) {
        this.borradorActual.setColorSecundario(colorSecundario);
    }

    // cuando querramos crear la prenda, el borrador hace las validaciones (para que ningun campo sea null como debe ser en una prenda)
    public void crearPrenda() {
        this.borradorActual.buildPrenda();
    }
}



//-------------------------------------QueMePongo3-------------------------------------
// --- recibir sugerencias de prendas: 
//      - debe vestir completamente
//      - cada parte del cuerpo 1 sola prenda
//      - deben poder combinar todas las prendas del guardarropas
//      - deben admitir filtros al generarse: por ej, filtrar ropa informal para mayores de 55 años
// --- indicar si una prenda es formal, informal o neutra
// --- cambiar el motor de sugerencias

// ESTILOS
// estiloPrenda -> enum (hace q la gente no pueda escribirlo mal)
public enum Estilo {
    INFORMAL, 
    FORMAL, 
    NEUTRA
}

// CLASE SUGERENCIA
// las sugerencias eligen de manera random una prenda para cada parte del cuerpo, el user solo la crea)
public class Sugerencia {
    private Prenda prendaParteSuperior;
    private Prenda prendaParteInferior;
    private Prenda prendaCalzado;

    public Sugerencia(Prenda prendaTorso, Prenda prendaPiernas, Prenda prendaPies) {
        // el Constructor hace que sean obligatorias
        this.prendaTorso = requireNonNull(prendaTorso, "campo obligatorio");
        this.prendaPiernas = requireNonNull(prendaPiernas, "campo obligatorio");
        this.prendaPies = requireNonNull(prendaPies, "campo obligatorio");
    }
}

// GUARDARROPAS
// 2 opciones:
// A) podemos tener esto (primera idea general)
public class Guardarropa {
    private List<Prenda> prendas;

    // para obtener las prendas de cierta categoria
    public List<Prenda> prendasDeCategoria(Categoria categoria) {
        return prendas.filter(prenda -> prenda.categoriaDeLaPrenda().equals(categoria));
    }
}

// B) o podemos tener algo asi --> tener separadas las prendas por categoria lo hace MAS EFICIENTE
public class Guardarropa {
    private List<Prenda> prendasParteSuperior;
    private List<Prenda> prendasParteInferior;
    private List<Prenda> prendasCalzado;

    // para obtener las prendas de cierta categoria --> no tenemos que estar filtrando siempre la lista
    public List<Prenda> prendasDeCategoria(Categoria categoria) {
        switch(categoria) {
            case PARTE_SUPERIOR:
                return prendasParteSuperior;
            case PARTE_INFERIOR:
                return prendasParteInferior;
            case CALZADO:
                return prendasCalzado;
        }
    }
}

// MOTOR DE SUGERENCIAS
// MotorSugerencias --> FACTORY METHOD = clase abstracta que tiene un método -> cuyas subclases implementarán
// los users tienen un atributo MotorSugerencias (el cual pueden cambiar) -> INYECCION DE DEPENDENCIAS
// método en la clase Usuario para generar la sugerencia
public class Usuario {
    // ... atributos ...
    Guardarropa guardarropa;
    MotorSugerencias motorSugerencias;

    public Sugerencia crearSugerencia() {
        return this.motorSugerencias.crearSugerencia(this.guardarropa);
    }

    public List<Sugerencia> crearTodasLasSugerenciasPosibles(){
        // para esto se podria usar un motor de sugerencias distinto que genere todas las combinaciones posibles
        // o meter un metodo en general (porque para todos los motores va a hacer lo mismo) --> producto cartesiano
        return this.motorSugerencias.crearTodasLasSugerenciasPosibles(this.guardarropa);
    }
}

public interface MotorSugerencias {
    // para una interfaz --> no hace falta poner ABSTRACT
    Sugerencia crearSugerencia(Guardarropa guardarropa);
    // se agrega DEFAULT porque es un metodo comun a todos los motores
    default List<Sugerencia> crearTodasLasSugerenciasPosibles(Guardarropa guardarropa) {
        // pseudocodigo --> no se como se hace el prod cartesiano
        return Set.cartesianProduct(
            guardarropa.prendasParteSuperior,
            guardarropa.prendasParteInferior,
            guardarropa.prendasCalzado
        )
    }
}

// por ejemplo
public class MotorSugerenciasBasico implements MotorSugerencias {
    @Override
    public Sugerencia crearSugerencia(Guardarropa guardarropa) {
        // obtenemos uno random de cada categoria
        Prenda prendaTorso = guardarropa.prendasDeCategoria(Categoria.PARTE_SUPERIOR).getRandom();
        Prenda prendaPiernas = guardarropa.prendasDeCategoria(Categoria.PARTE_INFERIOR).getRandom();
        Prenda prendaPies = guardarropa.prendasDeCategoria(Categoria.CALZADO).getRandom();

        return new Sugerencia(prendaTorso, prendaPiernas, prendaPies);
    }
}

// PD: para los filtros, simplemente se podria hacer que el usuario elija otro motor que filtre lo pedido --> lo malo: tendriamos muchos motores (motorParaMayoresDe65, etc)
// Oooo se podría usar el metodo DECORATOR:

// DECORATOR PATTERN
// - este metodo, hace que no tengamos tantas subclases por cada combinacion posible
// - en vez de crear subclases para cada motor (motorParaRopaInformalParaMayoresDe65), creamos un DECORADOR por cada filtro (por ej: ParaMayoresDe65, ParaClimaFrio, etc)
// - estos decoradores son sublcases de la principal (MotorSugerencias) y tienen un atributo MotorSugerencias (el que decoran)
// --> es decir, tenemos una subclase por cada agregado, pero no una por cada combinacion posible -> los agregados se envuelven entre si (se van "agregando" filtros)

// ejemplo con una pizza (easy)
// interfaz general --> MotorSugerencias
public interface Pizza {
    double costo();
    String descripcion();
}

// objeto base --> MotorSugerenciasBasico
public class Muzzarella implements Pizza {
    public double costo() { return 1000; }
    public String descripcion() { return "Pizza de Muzza"; }
}

// multiples filtros: DECORADORES --> motorParaRopaInformalParaMayoresDe65
public class ConJamon implements Pizza {    
    private Pizza pizzaQueEstoyEnvolviendo; // La pizza que está adentro mío --> por ejemplo Muzzarella --> DECORADO

    public ConJamon(Pizza pizza) {
        this.pizzaQueEstoyEnvolviendo = pizza;
    }

    public double costo() {
        // Mi costo es el costo de la pizza de adentro + 300 pesos del jamón
        return this.pizzaQueEstoyEnvolviendo.costo() + 300;
    }
}

public class ConTomate implements Pizza {    
    private Pizza pizzaQueEstoyEnvolviendo; // DECORADO

    public ConTomate(Pizza pizza) {
        this.pizzaQueEstoyEnvolviendo = pizza;
    }

    public double costo() {
        // Mi costo es el costo de la pizza de adentro + 200 pesos del tomate
        return this.pizzaQueEstoyEnvolviendo.costo() + 200;
    }
}

// uso: se van envolviendo
// 1. Creo una muzza simple (Costo: 1000)
Pizza miCena = new Muzzarella(); 

// 2. Le agrego jamón (Costo: 1000 + 300)
miCena = new ConJamon(miCena); 

// 3. Le agrego tomate (Costo: 1300 + 200)
miCena = new ConTomate(miCena); 

// queda algo como:
// System.out.println(miCena.costo()); Imprime 1600
// System.out.println(miCena.descripcion()); "Pizza de Muzza con Jamón con Jamón"



//-------------------------------------QueMePongo4-------------------------------------
// --- recibir sugerencias segun el clima con ropa segun condiciones climaticas
// --- conocer las referencias climaticas de BUENOS AIRES en un moment dado
// --- cada tipoPrenda tiene un rango de temperatura para la que es adecuada
// --- poder elegir distintos servicios externos facilmente
//      - accuweather nos da una sdk (biblioteca) para usarlo

// CONDICIONES CLIMATICAS
public class Usuario {
    // ... atributos ...
    Guardarropa guardarropa;
    MotorSugerencias motorSugerencias;
    ConocedorDeClima conocedor;

    void conocerClima(LocalDate momento, String condicionClimaticaAConocer){
        conocedor.conocerClima(momento, condicionClimaticaAConocer)
    }
}

// necesitamos un adapter para usar la AccuWeatherAPI ya que este devuelve una lista de varios atributos climaticos para una ciudad, cosa que no nos sirve para nuestra interfaz --> nosotros necesitamos la temperatura para Buenos Aires (o para hacerlo + extensible otras ciudades)
// interfaz saliente (a our sistema): ConocedorDelClima --> debemos crearla
// interfaz entrante: accuweatherAPI
// adapter: AccuWeatherAdapter --> implementa nuestra interfaz saliente (SIEMPRE) y overridea su metodo acomodando al metodo externo
// PD: un adapter tambien permite DESACOPLARNOS de la api de accuweather (ya que si mi codigo se basa en este: el vendor cambia algo --> mi programa deja de funcionar)
//      --> por esta razon no puedo delegarle toda la responsabilidad para crear la sugerencia a la api (siendo el crearSugerencias lo mas importante de mi programa)

// interfaz saliente
public interface ConocedorDeClima{
// devlverlo del tipo EstadoClima favorece mucho la abstraccion --> mas cohesion para mi sistema (antes que devolverlo del tipo list,map...)
    EstadoClima cononcerClima(String direccion);
}

// interfaz entrante dada (a la que necesitamos conectarnos)
public interface AccuWeatherAPI{
    List<Map<String,Object>> getWeather(String city);
}

public class EstadoClima{
    private BigDecimal temperatura;
    private BigDecimal humedad;
    // se podrian agregar otros ...
}

// adapter
// cada adapter implementa la interfaz saliente (lo hace ver como subclases y permite poder intercambiar entre ellas)
// importante: nunca podemos hacer que la interfaz externa implemente una nuestra!!!
// PD: si el user quiere cambiar de usar ACCU a otro, tan solo cambia su atributo adapter por el adaptador de otra interfaz --> se debe desarrollar otra clase adapter que transforme los datos para ese servicio

// ejemplo para accuweather
public class AccuWeatherAdapter implements ConocedorDeClima{
    // instancio una
    AccuWeatherAPI apiClima = new AccuWeatherAPI();
    
    @Override
    EstadoClima conocerClima(String direccion){
        // 1ero CONSULTO A LA API
        Map<String, Object> clima = consultarApi(direccion);
        // 2do TRANSFORMO DATOS Y LOS DEVUELVO
        return new EstadoClima(
            BigDecimal.ValueOf(clima.get("Temperatura")); // obtenemos la temperatura --> aca se deberia chequear cada campo que marca la api (si esta en celsius y sino hacer una cnversion, etc)
            BigDecimal.ValueOf(clima.get("Humedad")); // obtego la humedad
        )
    }

// se puede implementar un try & catch por si falla
    Map<String, Object> conultarApi(String direccion){
        try {
            return this.apiClima.getWeather(direccion).get(0);
        }
        catch {
            // lanzo una excepcion de mi dominio y hago lo que tenga que hacer para devolver un valor ok
        }
    }
}

// SUGERENCIAS SEGUN TEMPERATURA
public class Prenda{
    private TipoPrenda tipoPrenda;
    // ... atributos ...
    private BigDecimal temperaturaAdecuadaComoMaxima;

    public Bool esAptaPara(BigDecimal temperaturaActual) {
        return this.temperaturaAdecuadaComoMaxima >= tempreaturaActual
    }
}

public class Usuario{
    private String direccion;
    // el problema mas dificil es obtener la temperatura (en el adapter), filtrar las prendas es secundario
    // no hacia falta complicarse tanto devolviendo una condicion -- solo pide segun TEMPERATURA ---> esta solucion ESTA MAL
    public Sugerencia crearSugerenciaSegunClima() {
        List<Map<String,Object>> condiciones = conocerClima(this.direccion);
        return this.motorSugerencias.crearSugerenciaSegunClima(this.guardarropa, condiciones);
    }
    
    // sugerencias SEGUN TEMPERATURA --> es un int ---> esta BIEN
    public Sugerencia crearSugerenciaSegunClima() {
    EstadoClima clima conocerClima();
    return this.motorSugerencias.crearSugerenciaSegunClima(this.guardarropa, clima.getTemperatura());
    }
}

// agrego el metodo al motor sugerencias
public interface MotorSugerencias {
    // le paso como atributo las condiciones y luego cada motor lo filtra y luego como lo hace
    Sugerencia crearSugerenciaSegunClima(Guardarropa guardarropa, BigDecimal temperatura){
        
    }
}

public abstract class MotorSugerencias {
    // método principal que ahora filtra por temperatura
    public Sugerencia crearSugerenciaSegunClima(Guardarropa guardarropa, BigDecimal temperatura) {
        List<Prenda> superioresAptas = filtrarPrendas(guardarropa.getPrendasSuperiores(), temperatura);
        List<Prenda> inferioresAptas = filtrarPrendas(guardarropa.getPrendasInferiores(), temperatura);
        List<Prenda> calzadoAptos = filtrarPrendas(guardarropa.getPrendasCalzado(), temperatura);
    }

    private List<Prenda> filtrarPrendas(List<Prenda> prendas, BigDecimal temperatura) {
        return prendas.filter(prenda -> prenda.esAptaPara(temperatura))
    }
}

// Ver cualidades de diseño --> sirven para justificar


// ----- Reificar comportamiento (del apunte)
// VENTAJAS:
// Almacenar acciones pendientes hasta que deban ser ejecutadas
// Aprobación y rechazo de acciones antes de ejecutarlas
// Deshacer acciones realizadas
// Desacoplar la ejecución del flujo de control (asincronismo)


// ----- Ejemplo
// requerimientos (que fueron escalando)
// 1. Como tenedor de tarjetas de crédito, quiero poder ‘programar’ los pagos que se realicen de trasnoche, para que sean impactados al dia siguiente
// 2. Como tenedor de tarjetas de crédito, quiero poder elegir el monto a pagar para poder cubrir parte de la deuda -evitando que genere intereses- sin quedarme sin saldo en la cuenta
// 3. Como tenedor de tarjetas de crédito, quiero poder realizar compras de trasnoche y que estas sean impactados al dia siguiente
// inicialmente contabamos con una clase Cuenta, una clase Tarjeta y un enum Operador


enum Operador {
 VISA,
 AMEX,
 MASTER
}

class Tarjeta {
  Operador operador;
  Double deuda;

  void pagar(Double monto){
    deuda -= monto;
  }

  void comprarPor(Double monto){
    deuda += monto;
  }
}

class Cuenta {
  String cbu;
  Double saldo;
  List<Tarjeta> tarjetas;
  // esta lista permite guardarlas en el tiempo para aceptarlas/rechazarlas cuando se quiera
  List<Solicitud> solicitudesPendientes;

// OPERACIONES disponibles que tiene una persona con cuenta:
  void pagarTarjeta(Tarjeta tarjeta, Double montoAPagar){
    if(esHorarioDeClearing()){
      agregarSolicitud(new Pago(tarjeta,montoAPagar))
    }else{
        impactarPago(tarjeta, montoAPagar)
    }
  }

  void procesarCompra(Tarjeta tarjeta, Monto monto){
    if(esHorarioDeClearing()){
      agregarSolicitud(new Compra(tarjeta,monto))
    }else{
        tarjeta.pagar(monto);
    }
  }

// operaciones ASINCRONAS disponibles (cuando se decida, se las llama: se confirman las que habiamos programado):
    void efectivizarSolicitud(SolicitudPago solicitud) {
        quitarSolicitudPago(solicitud);
        solicitud.aplicarEn(this);
  }

    void impactarPago(Tarjeta tarjeta, Double montoAPagar) {
    Double montoPosible = Math.min(this.saldo, montoAPagar);
    tarjeta.pagar(montoPosible)
    saldo -= montoPosible
  }
}


// SOLICITUDES:
// al ser interface: permite diferentes solicitudes con diferentes parametros (permite polimorfismo)
// aplicar() method: que se refiere a pagar() o comprar() --> siempre esta en estas clases "cosificadas" que permiten acciones diferidas en el tiempo
// PD: de casualidad las 2 solicitudes del problema tienen 2 parametros
interface Solicitud {
  void aplicarEn(Cuenta cuenta);
}

// de pago
class Pago implements Solicitud{
  Tarjeta tarjeta;
  Double monto;

  aplicarEn(Cuenta cuenta){
    cuenta.impactarPago(this.getTarjeta(), this.getMonto())
  }
}

// de compra
class Compra implements Solicitud{
  Tarjeta tarjeta;
  Double monto;

  aplicarEn(Cuenta cuenta){
    this.getTarjeta().pagar(this.getMonto())
  }
}

// COMMAND PATHERN
// plasma estas ideas de asincronismo/reificar comportamiento
// convierte una solicitud o una acción en un objeto independiente

// estructura:
// 1)! Command: interfaz o clase abstracta que define el método de ejecución (apply() o execute()) --> Solicitud
// 2)! Concrete Command: clase que implementa al command. Tiene una referencia al reciever, y al hacer apply() llama a uno de sus metodos --> SolcitudPago, SolicitudCompra
// 3)! Receiver: contiene la lógica de negocio, sabe en serio cómo hacer su tarea --> Cuenta, Tarjeta (tarea que saben hacer: impactarPago(), pagar())
// 4)! Client: crea el objeto Concrete Command y le asigna su Receiver --> Cuenta (deberia ser una AppBancaria)
// 5) Invoker: se encarga de ejecutar la solicitud, guardando una referencia al command y sabe cuando dispararla --> tambien la Cuenta (deberia ser un GestorSolicitudes)

// PD: Cuenta --> god object



//-------------------------------------QueMePongo5-------------------------------------
// --- manejar varios guardarropas para separar mi ropa segun criterios
// --- crear guardarropas compartidos
// --- propuestas de agregado/eliminación de prendas
// --- ver propuestas y poder aceptarlas/rechazarlas
// --- poder deshacer las propuestas aceptadas --> para esto necesito las propuestas aceptadas


// manejo de varios guardarropas --> se arman segun criterio del user (cada uno elige lo que pone o no en ropadeVeranoGuardarropa, es subjetivo, no puedo filtrar)
public class Usuario {
    // ... atributos ...
    List<Guardarropa> guardarropas;
}

// el guardarropa compartido no necesita tener la lista de usuarios --> para que?
// simplemente es un guardarropas más, que en lugar de conocerlo una sola persona, lo conocen varias
// se pueden compartir
class Guardarropa {
    List<Propuesta> propuestasRecibidas;
    // List<Propuesta> propuestasAceptadas; --> en lugar de esto, se podria agregar un atributo booleano a Propuesta para saber si fue aceptada o no y de ahi filtrar estas / un enum para su estado

  void compatirCon(usuario) {
    usuario.agregarGuardarropa(this);
  }

  public List<Propuesta> getPropuestasPendientes() {
        return this.propuestas.stream()
            .filter(p -> p.getEstado() == Estado.PENDIENTE)
            .collect(Collectors.toList());
    }

// para las solicitudes
  void recibirPropuesta(Propuesta propuesta) {
    this.propuestasRecibidas.add(propuesta);
  }
}

// despues el usuario hace:
usuario.agregarGuardarropa(guardarropaCompartido);


// PROPUESTAS
// en este caso
// command: Propuesta (method execute(): aceptar() y unexecute(): deshacer()))
// abstract command: PropuestaEliminacion, PropuestaAgregacion (referencia al guardarropa, al sobreescribir execute() llama a alguno de sus metodos) (y tambien conoce a la prenda)
// reciever: guardarropa --> sabe agregarPrenda, eliminarPrenda
// client/invoker: Usuario --> crea una solicitud para tal guardarropa (le asigna este guardarropa)

// PD: template method para aceptar/rechazar ya que comparten la asignacion del estado
// tambien puedo no repetir el parametro Prenda, ambos lo tienen
public abstract class Propuesta {
    Prenda prenda;
    private Estado estado;
    void aceptar(Guardarropa guardarropa) {
        this.estado = Estado.ACEPTADA;
        applyLogicaDeAceptacion(guardarropa);
    }

    void rechazar(Guardarropa guardarropa) {
    this.estado = Estado.RECHAZADA;
    }

    void deshacer(Guardarropa guardarropa) {
        this.estado = Estado.PENDIENTE;
        applyLogicaDeDeshacer(guardarropa);
    };
}

public enum Estado {
    PENDIENTE, // apenas se crea una solicitud
    ACEPTADA,
    RECHAZADA
}
// PD: para el estado, decidi hacer un enum ya que en un futuro se podria pedir deshacer una propuesta rechazada, o simplemente verlas --> el historial es la lista de solicitudes misma, nunca las borro de esa lista
// si hubiera hecho un booleano fueAceptada() : la distinción entre "Pendiente" y "Rechazada" se pierde

// abstract command
// pd: la propuesta de aceptacion, no necesita tener al guardarropa como atributo, ya que:
//      - EL GUARDARROPA es quien conoce a las solicitudes, si hacemos que la relacion sea bidireccional, creamos una referencia circular
//      - el usuario sabe que prenda proponer, pero puede cambiar el guardarropa al que se la propone tranquilamente
// el "QUE" se hace (se propone una PRENDA) --> va como atributo
// el a "QUIEN" o "DONDE" se lo hace (a un GUARDARROPA) --> va como parametro (del apply())

public class PropuestaAgregacion implements Propuesta {
    void applyLogicaDeAceptacion(Guardarropa guardarropa) {
        guardarropa.agregarPrenda(prenda);
    }
    void applyLogicaDeDeshacer(Guardarropa guardarropa) {
        guardarropa.eliminarPrenda(prenda);
    }
}

// abstract command
public class PropuestaEliminacion implements Propuesta {
    void applyLogicaDeAceptacion(Guardarropa guardarropa) {
        guardarropa.eliminarPrenda(prenda);
    }
    void applyLogicaDeDeshacer(Guardarropa guardarropa) {
        guardarropa.agregarPrenda(prenda);
    }
}

// metodos en el guardarropa --> correspondientes al reciever
public class Guardarropa {
    // ... atributos ...
    void agregarPrenda(Prenda prenda) {
        this.getPrendas().add(prenda);
    }

    void eliminarPrenda(Prenda prenda) {
        this.getPrendas().remove(prenda);
    }
}



//-------------------------------------QueMePongo6-------------------------------------
// --- tener sugerencia diaria de que ponerse, actualizada
// --- disparar el calculo de sugerencias diarias para todos los users (a principio de dia)
//     - necesitamos un proceso que actualice la lista de estas sugerencias
// --- ver el historial de alertas meteorologicas
// --- al haber alguna alerta, se debe actualizar la sugerencia diaria
// --- ante alerta metereologica
//     - se debe sugerir llevar paraguas --> ante alerta de tormenta
//     - se debe sugerir evitar salir en auto --> ante alerta de granizo
//     - se debe enviar un mail notificando la alerta
//          ---> se quiere configurar estas acciones (agregar/sacar) ademas de soportar alertas a futuro

// SUGERENCIAS DIARIAS
class Usuario {
  Sugerencia sugerenciaDiaria;

  Sugerencia getSugerenciaDiaria() {
    return this.sugerenciaDiaria;
  }
}

// si modelara una lista de usuarios en el empleado, estaria mal --> cada empleado tendria una lista y ademas se va de nuestro dominio
class RepositorioUsuarios {
    List <Usuario> usuarios

    List <Usuario> getUsuarios() {
        return this.usuarios
    }
}

// clase que se dedica a generar las sugerencias diarias
// PD: como SOLO tengo el metodo calcularSugDiarias que referencia repo, se podria meter en el repositorio:
//  - pierdo algo de abstraccion
//  - gano al no complicarme tanto creando un objeto nuevo
//  - si en un futuro necesito mas acciones que las sugerencias diarias, estaria mejor esta clase
class AsesorImagen {
    RepositorioUsuarios repositorio

    void calcularSugerenciasDiarias() {
        repositorio.getUsuarios().forEach(user -> user.calcularSugerenciaDiaria)
    }
}

// ALERTAS
public enum AlertaMeteorologica {
    TORMENTA, GRANIZO
}

// interfaz saliente a nuestro sistema
public class conocedorDeAlertas {
    String direccion;
    List<AlertaMeteorologica> conocerAlertas(direccion);
}

// adapter
public class AccuWeatherAlertaAdapter implements conocedorDeAlertas {
    AccuWeatherAPI apiClima
    
private Map<String, AlertaMeteorologica> diccionarioAlertas;

    public AccuWeatherAlertaAdapter() {
        this.apiClima = new AccuWeatherAPI();
        this.diccionarioAlertas = new HashMap<>();
        // Mapeamos lo que dice la API a lo que entiende nuestro sistema
        diccionarioAlertas.put("STORM", AlertaMeteorologica.TORMENTA);
        diccionarioAlertas.put("HEAVY_RAIN", AlertaMeteorologica.TORMENTA); // Mapeamos dos cosas a una
        diccionarioAlertas.put("HAIL", AlertaMeteorologica.GRANIZO);
    }

    @Override
    public List<AlertaMeteorologica> conocerAlertas(String direccion){
        // La API devuelve algo tipo: ["STORM", "HUMIDITY"]
        List<String> alertasApi = this.consultarApi(direccion); 

        // Transformar
        return alertasApi
            // Buscamos la traducción en el mapa
            .map(alertaString -> diccionarioAlertas.get(alertaString)) 
            // Filtramos los nulos (por si la API manda algo que no nos importa, ej: "HUMIDITY")
            .filter(Objects::nonNull) 
    }
    
    Map<String, Object> consultarApi(String direccion){
        try{
            this.apiAlertas.getAlertas(direccion)
        }
        catch{
            // excepcion y logica que haga lo que se deba cuando ocurre error
        }
    }
}

// registro de alertas --> las ejecuta periodicamente
// no lo puede hacer el repositorio porque este se encarga solamente de guardar y recuperar datos (CRUD)
class RegistroAlertas {
    List<AlertaMeteorologica> alertasActuales;
    RepositorioUsuarios repositorio;
    
    void actualizarAlertas() {
		this.alertasActuales = new ServicioMeteorologicoAccuWeather().getAlertasMeteorologicas();
        this.getRepositorio().getUsuarios().forEach(user -> user.crearSugerenciaSegunClima())
        this.getRepositorio().getUsuarios().forEach(user -> user.reaccionarAnteAlertas(alertasActuales))
	 }
}

// cada vez que se actualizan las alertas, se disparan acciones:
// interfaces salientes para los servicios externos (por ahora nos despreocupamos por la traduccion)
interface Correo{
  public enviarCorreo(Usuario user, String cuerpo);
}

interface Notificador{
  public notificar(String mensaje);
}

// el user conoce sus acciones configurables
class Usuario {
    //...atributos...
    List<AccionConfigurable> accionesConfigurables;

    // puede agregar/sacar cuando desee
    void agregarAccion(AccionConfigurable accion) {
        this.getAccionesConfigurables().add(accion)
    }
    void sacarAccion(AccionConfigurable accion) {
        this.gerAccionesConfigurables().remove(accion)
    }

    void reaccionarAnteAlertas(List<AlertaMeteorologica> alertas) {
        this.getAccionesConfigurables().forEach(accion -> accion.reaccionar(this, alertas))
    }
}

// acciones configurables --> observadores
public interface AccionConfigurable {
    void reaccionar(Usuario user, List<AlertaMeteorologica> alertas)
}

public class MandarMail extends AccionConfigurable {
    @Override reaccionar(Usuario user, List<AlertaMeteorologica> alertas){
        new Correo().enviarCorreo(user,alertas)
    }
}

// para matchear la alerta con el mensaje, usamos un diccionario, ya que si usamos if queda un type test medio feo y ademas a nivel interfaz web no se podria implementar
public class NotificadorAnteAlertas implements AccionConfigurable {
  Map<AlertaMeteorologica, String> mensajes = {...}

	reaccionar(user, alertas) {
    alertas.forEach(alerta -> new Notificador().notificar(mensajes.get(alerta)))
	}
}



//-------------------------------------QueMePongo6 PUNTO BONUS-------------------------------------
// palabras sabias de franco:
// planificacion interna: necesita tener mi aplicacion corriendo siempre para que funciona
// - si se cae mi proceso/app/servidor estas acciones se pierden
// ---> conviene usarla para casos en los que se necesita tener corriendo un proceso en un periodo corto y que si se cae no pase nada

// planificacion externa: es externo a mi app, se encarga el so
// - no necesita tener corriendo mi app
// ---> conviene usarlo para tareas que queremos que se sincronicen adecuadamente y ademas son mas faciles de escalar (2do parcial)

// requerimientos
// Como administradore de QueMePongo, quiero que las sugerencias diarias se calculen automáticamente sin que un empleado necesite disparar esta acción manualmente
// Como administradore de QueMePongo, quiero que las alertas se publiquen en el sitio automáticamente sin que un empleado necesite disparar esta acción manualmente

// necesidades para planificacion externa:
// - Metodo que necesita ser ejecutado periodicamente
// - Main()
// - Comando del CronTab (corre el main)


// esto pasaria si ambas funciones corrieran con la misma frecuencia
// 1)
public class QueMePongoApp{
    public static void main(String[],Args[]){
        // instancio las clases que sean necesarias para correr mi metodo (si el repo fuera un singleton no hace falta)
        RepositorioUsuarios repo = new RepositorioUsuarios()
        AsesorImagen asesorImagen = new AsesorImagen()
        RegistroAlertas registro = new RegistroAlertas();
        
        // corro los metodos
        asesorImagen.calcularSugerenciasDiarias(repo);
        registro.actulizarAlertas();
    }
}
// 2) despues de esto deberia cambiar en el pom.xml el assembly plugin agregandole el nombre de mi clase que contiene el main()
// 3) corro mi app con mvn verify y se genera un .jar
// 4) creo un crontab en mi consola (del sisitema operativo) y le paso para que corra el .jar
//   min / hour / dayofmonth / month / dayofweek / comando
    
//    0 6 * * * java -jar /home/user/quemepongo.jar
//    --> corre todos los dias a las 6 am


// pero como quiero que una tarea se ejecute diariamente, y otra cada 30 min --> debo hacer 2 main
public class GeneradorSugerencias {
    public static void main(String[] args) {
        RepositorioUsuarios repo = new RepositorioUsuarios();
        AsesorImagen asesorImagen = new AsesorImagen();
        
        asesorImagen.calcularSugerenciasDiarias(repo);
    }
}
// cron: 0 6 * * * java -jar /home/user/quemepongo.jar quemepongo.main.GeneradorSugerencias --> diariamente a las 6 am

public class ActualizadorAlertas {
    public static void main(String[] args) {
        RepositorioUsuarios repo = new RepositorioUsuarios(); 
        RegistroAlertas registro = new RegistroAlertas();
 
        registro.actualizarAlertas(repo); 
    }
}
// cron: */30 * * * * java -jar /home/user/quemepongo.jar quemepongo.main.ActualizadorAlertas --> cada 30 min



//-------------------------------------PARCIALES-------------------------------------

// ------------------------------------NOODLE
// https://docs.google.com/document/d/1-Qpv38kB29lNuzIi88TkTg4LGbO_iNijoVesZyvG5wM/edit?usp=sharing

public class Alumno {
    String nombre;
    String email;
}

public class Curso{
    List<Alumno> alumnos;
    List<Grupo> grupos;
    Int tamanioGrupos

    void armarGrupos()

    List<Grupo> getGruposCerrados(){
    return this.grupos.filter(g -> g.estaCerrado())
    }
}

public class Grupo {
    private String nombre;
    private List<Alumno> integrantes;
    private List<Solicitud> solicitudes; // command
    private List<AccionAsociada> accionesAsociadas; // observers
    private boolean estaCerrado;

// logica de COMMAND (solicitudes)
    public void recibirSolicitud(Solicitud solicitud) {
        this.solicitudesPendientes.add(solicitud);
    }

    public void aprobarSolicitud(Solicitud solicitud) {
        solicitud.serAplicadaEn(this); 
        this.solicitudesPendientes.remove(solicitud);
    }

// logica de OBSERVERS (acciones asignadas)
    public void agregarIntegrante(Alumno alumno) {
        this.integrantes.add(alumno);
        this.notificarAlta(alumno);
    }

    public void removerIntegrante(Alumno alumno) {
        this.integrantes.remove(alumno);
        this.notificarBaja(alumno);
    }

    // métodos que avisan a las AccionesAsociadas
    private void notificarAlta(Alumno alumno) {
        this.accionesAsociadas.forEach(accion -> accion.notificarAlta(this, alumno));
    }

    private void notificarBaja(Alumno alumno) {
        this.accionesAsociadas.forEach(accion -> accion.notificarBaja(this, alumno));
    }

    public void agregarAccion(AccionAsociada accion) {
        this.accionesAsociadas.add(accion);
    } // lo mismo para sacar la accion


    // para ultimo punto
    void habilitarEntrega(entrega){
    this.entregashabilitadas.add(entrega)
    new EnviarMail().notificarEntrega(this, entrega)
    }

}


// COMMAND
public interface Solicitud {
    void serAplicadaEn(Grupo grupo);
}

public class AltaAlumno implements Solicitud {
    private Alumno alumno;

    @Override
    public void serAplicadaEn(Grupo grupo) {
        grupo.agregarIntegrante(this.alumno);
    }
}

public class BajaAlumno implements Solicitud {
    private Alumno alumno;

    @Override
    public void serAplicadoEn(Grupo grupo) {
        grupo.removerIntegrante(this.alumno);
    }
}

// OBSERVER
public interface AccionAsociada {
    void notificarAlta(Grupo grupo, Alumno alumno);
    void notificarBaja(Grupo grupo, Alumno alumno);
    void notificarCierre(Grupo grupo, Alumno alumno);
}

// estas clases hacen de OBSERVADORES y ademas de ADAPTERS para las librerías externa
public class CrearRepositorio implements AccionAsociada {
    private GuitabSDK guitab; 

    @Override
    public void notificarAlta(Grupo grupo, Alumno alumno) {
        guitab.addMember(grupo.getNombre(), alumno.getEmail());
    }

    @Override
    public void notificarBaja(Grupo grupo, Alumno alumno) {
        guitab.removeMember(grupo.getNombre(), alumno.getEmail());
    }

    @Override
    notificarCierre(grupo){
    new GuitabSDK().crearRepoConAccessos("repo_" + grupo.getNombre(),grupo.getUsernames())
    }

}

public class DarPermisosRepositorio implements AccionAsociada {
    private GuitabSDK guitab; 

    // chequeo que el grupo no este cerrado
    @Override
    public void notificarAlta(Grupo grupo, Alumno alumno) {
        if(grupo.estaCerrado()){
        new GuitabSDK().darAcceso("repo_" + grupo.getNombre(),alumno.getGitUsername())
        }
    }

    @Override
    public void notificarBaja(Grupo grupo, Alumno alumno) {
    }
}

public class SacarPermisosRepositorio implements AccionAsociada {
    private GuitabSDK guitab; 

    @Override
    public void notificarAlta(Grupo grupo, Alumno alumno) {
        if(grupo.estaCerrado()){
        new GuitabSDK().quitarAcceso("repo_" + grupo.getNombre(),alumno.getGitUsername())
        }
    }

    @Override
    public void notificarBaja(Grupo grupo, Alumno alumno) {
    }
}

public class NotificarAlumnos implements AccionAsociada {
    private MailSender mailSender = new MailSender();

    @Override
    public void notificarAlta(Grupo grupo, Alumno alumno) {
        mailSender.send(alumno.getEmail(), "Bienvenido al grupo!");
    }

    @Override
    public void notificarBaja(Grupo grupo, Alumno alumno) {
        mailSender.send(grupo.getEmails(), "Baja Integrante", "Se ha retirado" + integrante.getNombre())
    }
    
    @Override
    notificarCierre(grupo, alumno){
    new MailSender().send(grupo.getEmails(), "Se ha cerrado el grupo")
    }

}

// librerias externas
class GuitabSDK { 
    void addMember(String repoName, String email) { } 
    void removeMember(String repoName, String email) { } 
}
class MailSender { 
    void send(String to, String body) { } 
}


// ENTREGAS
public class Asignacion{
    Titulo titulo;
    List<Entrega> entregas;
    Entrega entregaActual;

    void avanzarEntrega{
        this.entregaActual = this.entregas[this.entregas.indexOf(this.entregaActual) + 1]
    }
}

public class Entrega{
    String url;
}

public enum Titulo{
    TRABAJO_PRACTICO, ACTIVIDAD_DE_CLASE
}

public class RepositorioAsignaciones{
    List<Asignacion> asignaciones

    void distribuirEntregas{
        Curso.getinstance().getGruposCerrados().forEach(g -> this.habilitarEntregas(g))
    }

    // este es el metodo que va a correr el cron
    void habilitarEntregas(grupo){
    this.asignaciones.forEach(a -> {
    grupo.habilitarEntrega(a.getEntregaActual())
    a.avanzarEntrega()})
    }
}

// MAIN para crontab
public class HabilitadorEntregas {
    public static void main(String[] args) {
        RepositorioAsignaciones repo = new RepositorioAsignaciones();
        
        repo.distribuirEntregas();
    }
}
// cron: * * */1 * * java -jar /home/user/app.jar app.main.HabilitadorEntregas --> semanalmente


//-------------------------------------PARTE 4 DE LIVRESTREAM (chat gpt)
// ---- Requerimiento 1
class Usuario {
    SistemaNotificacion sistemaNotificacion;
    donar(Donacion donacion, Transmision transmision){
        // y aplico la logica de la donacion
        donacion.donarSegunTipo(transmision)
    }
    void notificarInicio(Canal canal) {
        this.sistemaNotificacion.enviarNotificacion(this);
    }
}

abstract class Donacion {
    int costoBase;
    void donarSegunTipo(Transmision transmision);
    int comision();
    int costoAgregado();

    // TEMPLATE METHOD con costoAgregado y comision
    int recaudacionNeta(){
        return this.costoBase + this.costoAgregado - this.comision();
    }
}

class DonacionDirecta extends Donacion {
    String mensaje;

    void donarSegunTipo(Transmision transmision){
        transmision.recibirDinero(this.dinero)
        transmision.recibirMensaje(this.mensaje)
    }

    int costoAgregado() {
        return 0;
    }
    int comision (){
        return this.costoAgregado() * 0.05;
    }
}

class SuperMensaje extends Donacion {
    int segundos;
    String mensaje;

    int costoAgregado(){
        return this.segundos * 10;
    }
    int comision(){
        return this.costoAgregado() * 0.1;
    }
}

class LLuviaSuscripciones extends Donacion {
    void donarSegunTipo(Transmision transmision){
        transmision.viewers.getRandom(5).forEach(viewer -> viewer.aceptarSuscripcion())
    }

    int costoAgregado(){
        return 0;
    }
    int comision(){
        return 0;
    }
}

class Transmision {
    List<Donacion> donaciones;
    List<Moderacion> moderaciones;

    List<Donacion> listarDonaciones(){
        return this.donaciones;
    }
    Int totalRecaudado(){
        return this.donaciones.forEach(donacion -> donacion.recaudacionNeta()).sum()
    }
}


// ---- Requerimiento 2
class SistemaNotificacion {
    enviarNotificacion()
}

// planteo OBSEVER para el canal --> canal: observado, usuarios: observadores
class Canal{
    List<Usuario> suscriptores;

    void iniciarTransmision(){
        Transmision transmision = new Transmision();
        this.suscriptores.forEach(suscriptor -> suscriptor.notificarInicio(this))
    }
}

// nuestra INTERFAZ SALIENTE para enviar notificaciones
interface SistemaNotificacion {
    void enviarNotificacion(Usuario usuario);
}

// cada uno de estos hace de ADAPTER --> transforma el metodo de los sdk en valores que nos sirvan, ademas se pueden agregar/cambiar facilmente
class Email implements SistemaNotificacion {
    EmailSDK emailSDK = new EmailSDK();
    @Override
    void enviarNotificacion(Usuario usuario){
        emailSDK.sendEmail(usuario.getEmail(), "¡Hola! Se ha iniciado una nueva transmisión en el canal que estas suscripto");
    }
}

class Push implements SistemaNotificacion {
    PushSDK pushSDK = new PushSDK();
    @Override
    void enviarNotificacion(Usuario usuario){
        pushSDK.sendPush(usuario.getPushToken(), "¡Hola! Se ha iniciado una nueva transmisión en el canal que estas suscripto");
    }
}

class Discord implements SistemaNotificacion {
    DiscordSDK discordSDK = new DiscordSDK();
    @Override
    void enviarNotificacion(Usuario usuario){
        discordSDK.sendMessage(usuario.getDiscordId(), "¡Hola! Se ha iniciado una nueva transmisión en el canal que estas suscripto");
    }
}

// ---- Requerimiento 3
interface Moderacion {
    void moderar(Transmision transmision);
}

class ModoSuscriptores implements Moderacion {
    @Override
    transmision.viewers.forEach(viewer -> {
        if(!viewer.esSuscriptor()){
            transmision.moderaciones.add(new BloquearChat(viewer))
            }
        }
    )
}

class BloquearChat implements Moderacion {
    Usuario viewer;
    @Override
    void moderar(Transmision transmision){
        transmision.chat.remove(this.viewer)
    }
}

// por ej, si quiero listar las donaciones diariamente a las 8am:
public class ListadorDeDonaciones {
    public static void main(String[] args) {
        // si paso la transmision asi, se crearia una transmision sin datos en memoria
        Transmision transmision = new Transmision();
        // por lo que deberia tener un repositorio de trasmisiones para ir acumulando las diarias
        RepositorioTrabsmisiones repo = new RepositorioTransmisiones();
        repo.transmisiones.forEach(transmision -> transmision.listarDonaciones());
    }
}

// en un cron: 0 8 * * * java -jar /home/user/app app.main().ListadorDeDonaciones
