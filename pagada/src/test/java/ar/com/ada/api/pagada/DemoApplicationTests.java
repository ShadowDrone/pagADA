package ar.com.ada.api.pagada;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.com.ada.api.pagada.services.*;
import ar.com.ada.api.pagada.services.DeudorService.DeudorValidacionEnum;
import ar.com.ada.api.pagada.entities.*;
import ar.com.ada.api.pagada.services.EmpresaService.EmpresaValidacionEnum;
import ar.com.ada.api.pagada.entities.Pais.*;
import ar.com.ada.api.pagada.entities.Servicio.EstadoEnum;
import ar.com.ada.api.pagada.entities.Servicio.TipoComprobanteEnum;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	EmpresaService empresaService;
	@Autowired
	DeudorService deudorService;
	@Autowired
	ServicioService servicioService;

	// Pedidos originalmente para empresas:
	// Test1: que valide el Id Impositivo con valores correctos
	// Test2: que valide el Id Impositivo con valores incorrectos

	// Empresa: que tipos de testings unitarios podriamos hacer:
	// ETU1(Empresa Test Unitario 1): enviar letras dentro del Id Impositivo ->
	// esperamos que de error
	// ETU2: enviar nombre null
	// ETU3: id_impositivo null
	// ETU4: pais que no este en la lista de paises.
	// ETU5: nombre supere los 100 caracteres.
	// ETU6: enviar todos los datos correctos.
	// ETU7: crear y verficar que la empresa se creo.

	// Testear si 1 = 1

	// Test siempre devuelven void
	// Tests NO tienen parametros
	// Tests se llaman como quieran: pero tratar de que el nombre identifique lo que
	// estamos testeando
	// Se pueden usar la clases services y repos, usando el @Autowired
	// Test DATA debe impactar sobre una base de datos de Testing o una en Memoria

	@Test
	void Empresa_IdImpositivo_QueFalle_ConLetras() {

		EmpresaValidacionEnum resultado;

		// Harcodeo la creacion de una instancia de Empresa

		Empresa emp = new Empresa();
		emp.setPaisId(32);
		emp.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		emp.setIdImpositivo("3373737A"); // <-- aca tiene una letra.
		emp.setNombre("ADA TESTIN EMPRESA");

		resultado = empresaService.validarEmpresa(emp);

		// Esperamos qeu de error: ID_IMPOSITIVO_INVALIDO;
		// assert espera una afirmacion.
		assertTrue(resultado == EmpresaValidacionEnum.ID_IMPOSITIVO_INVALIDO);

	}

	@Test
	void Empresa_nombre_null() {
		// System.out.println("************* INICIO TEST EMPRESA NULL ");
		EmpresaValidacionEnum resultado;
		// lo ponemos en nulo
		Empresa empresa = new Empresa();

		empresa.setPaisId(32);
		empresa.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		empresa.setIdImpositivo("3373732547875");
		empresa.setNombre(null);// pusimos en nulo el nombre
		resultado = empresaService.validarEmpresa(empresa);

		// System.out.println("El resultado fue "+resultado);
		// esperamos que de error :nombre invalido
		assertTrue(resultado == EmpresaValidacionEnum.NOMBRE_INVALIDO);
	}

	// ETU3: id_impositivo null
	@Test
	void Empresa_id_Impositivo_null() {

		EmpresaValidacionEnum resultado;
		Empresa empresa = new Empresa();
		empresa.setPaisId(32);
		empresa.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);// Seteamos como null al IdImpositivo
		empresa.setIdImpositivo(null);
		empresa.setNombre("Empresa Test");
		resultado = empresaService.validarEmpresa(empresa);

		assertTrue(resultado == EmpresaValidacionEnum.ID_IMPOSITIVO_INVALIDO);
	}

	// ETU5: nombre supere los 100 caracteres.
	@Test
	void Empresa_nombre_May_Cien() {
		EmpresaValidacionEnum resultado;
		Empresa empresa = new Empresa();
		empresa.setPaisId(32);
		empresa.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		empresa.setIdImpositivo("1234567891011");
		empresa.setNombre(
				"SaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaantiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiTalledoS.a");
		resultado = empresaService.validarEmpresa(empresa);

		assertTrue(resultado == EmpresaValidacionEnum.NOMBRE_INVALIDO);

	}

	// ETU6: \
	@Test
	void Empresa_datos_correctos() {
		EmpresaValidacionEnum resultado;

		Empresa empresa = new Empresa();
		empresa.setPaisId(32);
		empresa.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		empresa.setIdImpositivo("3373732547875");
		empresa.setNombre("ADA TESTIN EMPRESA");

		resultado = empresaService.validarEmpresa(empresa);

		assertTrue(resultado == EmpresaValidacionEnum.OK);// tiene que dar todo ok
	}

	// Lo mismo, pero ahora para deudor:
	@Test
	void Deudor_IdImpositivo_QueFalle_ConLetras() {

		DeudorValidacionEnum resultado;

		resultado = deudorService.validarDeudorInfo(32, TipoIdImpositivoEnum.CUIT, "7777777A7777776",
				"ADA TEST DEUDOR");

		assertTrue(resultado == DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO);

	}

	@Test
	void Deudor_nombre_null() {

		DeudorValidacionEnum resultado = deudorService.validarDeudorInfo(32, TipoIdImpositivoEnum.CUIL, "35501187899",
				null);

		assertTrue(resultado == DeudorValidacionEnum.NOMBRE_INVALIDO);
	}

	// ETU3: id_impositivo null

	@Test
	void Deudor_id_impositivo_null() {
		DeudorValidacionEnum resultado;
		Deudor deudor = new Deudor();
		deudor.setPaisId(32);
		deudor.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIL);
		deudor.setIdImpositivo(null);
		deudor.setNombre("santi santi");

		resultado = deudorService.validarDeudorInfo(deudor.getPaisId(), deudor.getTipoIdImpositivo(),
				deudor.getIdImpositivo(), deudor.getNombre());

		assertTrue(resultado == DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO);

	}

	// Nombre de deudor supere los 100 caracteres.
	@Test
	void Deudor_Nombre_Cien_letras() {
		DeudorValidacionEnum resultado;
		Deudor deudor = new Deudor();
		deudor.setPaisId(32);
		deudor.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		deudor.setIdImpositivo("234567891011");
		deudor.setNombre(
				"Lorem ipsum dolor sit amet consectetur adipiscing elit feugiat, facilisi est potenti dui morbi aliquet hac");
		resultado = deudorService.validarDeudorInfo(deudor.getPaisId(), deudor.getTipoIdImpositivo(),
				deudor.getIdImpositivo(), deudor.getNombre());

		assertTrue(resultado == DeudorValidacionEnum.NOMBRE_INVALIDO);

	}

	@Test
	void PagoInfoOfuscada() {
		Pago pago = new Pago();
		pago.setInfoMedioPago("123456789");
		String infoOfuscada = pago.ofuscarInfoMedioPago();
		assertEquals("*789", infoOfuscada);
	}

	/*
	 * 6) Anular un servicio: DELETE /api/servicios/{id} : debe poner en estado
	 * ANULADO a un servicio.
	 * 
	 * Hacer un Test qeu valide que cuando se "anule" un servicio mediante el
	 * service, verificar en la base de datos que haya sido realmente anulado.
	 */

	@Test
	void Servicio_Anulado() {

		// Como estamos en base de datos en memoria, para tener un Servicio a ANular,
		// tenemos que:
		// 1. Crear una empresa(INSTANCIAR Y GRABARLA EN LA DB)
		// 2. Crear un Deudor(instanciar y grabarlo en la db)
		// 3. Instanciar un servicio y ponerle valores buenos para que se pueda anular
		// 4. Grabar el servicio
		// 5. Anular el servicio
		// 6. Grabar el servicio anulado
		// 7. Buscar el servicio, por ID
		// 8. verificar con "assert" que el estado sea anulado

		// 1.
		Empresa empresa = new Empresa();
		empresa.setPaisId(32);
		empresa.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		empresa.setIdImpositivo("4337373425478788");
		empresa.setNombre("ADA TESTIN EMPRESA");

		
		empresaService.crearEmpresa(empresa);

		// 2.
		Deudor deudor = deudorService.crearDeudor(32, TipoIdImpositivoEnum.CUIL, "45987845695388", "Santi Santi");

		//2.5
		TipoServicio tipoServicio = new TipoServicio();
		tipoServicio.setTipoServicioId(10);
		tipoServicio.setNombre("AGUA");
		//servicioService.buscarTipoServicioPorId(10);
		// 3.
		Servicio servicio = new Servicio();

		servicio.setEmpresa(empresa);
		servicio.setDeudor(deudor);
		servicio.setTipoServicio(tipoServicio);
		servicio.setTipoComprobante(TipoComprobanteEnum.IMPUESTO);
		servicio.setNumero("234526841569");
		servicio.setFechaEmision(new Date());
		servicio.setFechaVencimiento(new Date());
		servicio.setImporte(new BigDecimal(2000));
		servicio.setMoneda("ARS");
		servicio.setCodigoBarras("123456789");
		servicio.setEstadoId(EstadoEnum.PENDIENTE);
		// 4.
		servicioService.crearSevicio(servicio);

		Integer servicioId = servicio.getServicioId();
		// 5.
		servicio.setEstadoId(EstadoEnum.ANULADO);
		// 6.
		servicioService.grabar(servicio);

		// 7.
		Servicio servicioBaseDatos = servicioService.buscarServicioPorId(servicioId);

		// 8.
		assertTrue(servicioBaseDatos.getEstadoId() == EstadoEnum.ANULADO);
	}

}