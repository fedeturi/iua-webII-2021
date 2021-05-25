// ESTE ES EL ARCHIVO QUE CONTIENE EL CONTROLADOR DE LA APLICACION. COMO ES UN APP SENCILLA USAMOS UN UNICO CONTROLLE
// A PROPOSITO, SE HA COLOCADO UN RACHIVO .JS EXTERNO PARA QUE VEAN QUE ANGULAR NOS DA LA POSIBILIDAD DE USAR MULTIPLES
// ARCHIVOS CON SCRIPTS. EN ESTE CASO EN EL ARCHIVO FUNCIONES SE HAN COLOCADO LAS FUNCIONES QUE ABRE ( MUESTRA)
// Y CIERRA (OCULTA) EL POPUP, QUE NO ES MAS QUE UN DIV QUE RECIBE LA INFO QUE TENEMOS QUE EDITAR


// ESTAS DOS PRIMERAS LINEAS SON ESTANDAR Y SE USAN SIEMRE EXACTAMENTE IGUAL, PUES ES LA DECLARACION DEL CONTROLLER
// LO UNICO QUE CAMBIA SON LOS NOMBRES DE LA APLICACION Y DEL CONTROLLER
// OBSERVEN AQUI QUE TODO LO QUE QUERRAMOS QUE SEA ACCESIBLE EN LA VISTA HTML, TANTO MODELOS (VARIABLES) COMO FUNCIONES
// DEBEN ESTAR VINCULADAS AL $scope 

var app = angular.module("App", []);
app.controller('control', function ($scope) 
{

	$scope.getColorTabla = function (imc) {    // DECLARACION DE UNA FUNCION
		if (imc <= 25) {
			return "verdeTabla";
		}
		if (imc > 25 && imc <= 30) {
			return "amarilloTabla";  // SON CLASES DE ESTILO DEFINIDOS EN EL ARCHIVO CSS 
		} else {
			return "rojoTabla";      // SON CLASES DE ESTILO DEFINIDOS EN EL ARCHIVO CSS 
		}
	}

	$scope.borrar = function (i) { // DECLARACION DE UNA FUNCION
		if (confirm("Desea Borrar al alumno " + $scope.alumnos[i].apellido + " ? La operación no se podrá deshacer")) 
		{
		$scope.alumnos.splice(i, 1);
		localStorage.setItem("alumnos", JSON.stringify($scope.alumnos));
		}
	}

	$scope.editar = function (i) {  // RECIBIMOS EL INDICE DEL ALUMNO QUE QUEREMOS EDITAR PARA MOSTRARLO EN EL POPUP
		var alumno = $scope.alumnos[i];
		$scope.indice = i;
		$scope.editApellido = alumno.apellido;
		$scope.editLegajo = alumno.legajo;
		$scope.editSexo = alumno.sexo;
		$scope.editPeso = alumno.peso;
		$scope.editAltura = alumno.altura;


		abrirPopup();

	}

	$scope.validar = function (entrada) {

		if (entrada == "") {
			var apellido = $scope.apellido;
			var legajo = $scope.legajo;
			var sexo = $scope.sexo;
			var peso = $scope.peso;
			var altura = $scope.altura;
		} else {
			var apellido = $scope[entrada + "Apellido"];
			var legajo = $scope[entrada + "Legajo"];
			var sexo = $scope[entrada + "Sexo"];
			var peso = $scope[entrada + "Peso"];
			var altura = $scope[entrada + "Altura"];
		}


		var error = "";

		if (!isValid(apellido) || !isValid(legajo) || !isValid(sexo) || !isValid(peso) || !isValid(altura)) {
			alert("No puede estar vacio ningun campo");
			return;
		}


		var NumLegajo = parseInt(legajo);
		var NumAltura = parseFloat(altura);
		var NumPeso = parseFloat(peso);


		if (apellido.length < 3) {
			error += "- El apellido debe tener al menos 3 letras.\n";
		}


		if (isNaN(legajo)) {
			error += "- El legajo no es un número.\n";
		} else if (NumLegajo < 0) {
			error += "- El legajo no puede ser un número negativo.\n";
		}

		if (isNaN(peso)) {
			error += "- El peso no es un número.\n";
		} else if (NumPeso < 0) {
			error += "- El peso no puede ser un número negativo.\n";
		}

		if (isNaN(altura)) {
			error += "- La altura no es un número.\n";
		} else if (NumAltura < 0) {
			error += "- La altura no puede ser un número negativo.\n";
		}

		if (error != "") {
			alert("Se encontraron los siguientes errores: \n" + error);
			return null;
		}

		var imc = peso / (altura / 100) ** 2;

		imc = imc.toFixed(2);
		var alumno = { 'apellido': apellido, 'legajo': legajo, 'sexo': sexo, 'peso': peso, 'altura': altura, 'imc': imc };
		return alumno;
	}

	$scope.registrarAlumno = function () {

		var alumnos = $scope.alumnos;
		var alumno = $scope.validar("");

		if (alumno == null) {
			return;
		}

		for (i in alumnos) {
			if (alumnos[i].legajo == alumno.legajo) {
				alert("Ya existe el Alumno");
				return;
			}
		}

		// variables accesibles en el html
		
		$scope.alumnos.push(alumno);
		localStorage.setItem("alumnos", JSON.stringify($scope.alumnos));

		$scope.apellido = "";
		$scope.legajo = "";
		$scope.sexo = "";
		$scope.peso = "";
		$scope.altura = "";
	}

	$scope.guardar = function () {
		var alumnos = $scope.alumnos;
		var alumno = $scope.validar("edit");

		if (alumno == null) {
			return;
		}

		for (i in alumnos) {
			if (i != $scope.indice && alumnos[i].legajo == alumno.legajo) {
				alert("Ya existe ese Legajo.");
				return;
			}
		}

		$scope.alumnos[$scope.indice] = alumno;
		localStorage.setItem("alumnos", JSON.stringify($scope.alumnos));

		cerrarPopup();
	}

	$scope.getSobrepeso = function () {
		var i;
		var total = 0;
		var alumnos = $scope.alumnos;

		for (i in alumnos) {
			if (alumnos[i].imc > 25) {
				total += 1;
			}
		}

		return total;
	}
	$scope.borrarLocalStorage = function () {
		if (confirm("Desea Borrar los Datos Almacenados localmente ? La operación no se podrá deshacer")) {
			localStorage.clear();
			location.reload(); // esta funcion recarga la página para reflejar los cambios al borrar los datos
		}

	}
	// 
	$scope.alumnos = JSON.parse(localStorage.getItem("alumnos"));
	if ($scope.alumnos == null) {
		$scope.alumnos = [];
		localStorage.setItem("alumnos", JSON.stringify($scope.alumnos));
	}
});