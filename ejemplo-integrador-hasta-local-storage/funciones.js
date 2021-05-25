
function cerrarPopup(){
	var c = document.getElementById("popup");
	c.className = "popup ocultarPopup container";
}
function abrirPopup(){
	var c = document.getElementById("popup");
	c.className = "popup mostrarPopup container";
}
function isValid(aux){
	return aux != undefined && aux != "";
}