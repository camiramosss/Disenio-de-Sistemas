// PRACTICA PARCIALES DISEÑO DE SISTEMAS (1ER PARCIAL)

//-------------------------------------MACOWINS-------------------------------------
// --- precio de venta de una prenda --> segun estado de prenda (nueva, promocion, liquidacion) + precio base
// --- tipo de prenda --> sacos, pantalones, camisas
// --- registro de ventas --> prendas vendidas, cantidad prendas vendidas, fecha de venta
// --- tipo de pago --> efectivo, tarjeta
// --- ganancias de un determinado dia

// Prendas
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



//-------------------------------------QUEMEPONGO1-------------------------------------
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



//-------------------------------------QUEMEPONGO2-------------------------------------
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



//-------------------------------------QUEMEPONGO3-------------------------------------
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



//-------------------------------------QMP4-------------------------------------
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
    ConocedorDeClimaAdapter conocedor;

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

    public Bool esAptaPara(BigDecimal temperaturaActual){
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