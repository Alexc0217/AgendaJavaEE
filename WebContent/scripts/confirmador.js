/**
 * confirmação de exclusão de um contato
	@author alexc0217

 */

function confirmar(idcon){
	let resposta = confirm("Confirma a exclusão desse contato? ")
	if(resposta === true){
		window.location.href = "delete?idcon=" + idcon
	}
}

function confirmarAll(){
	let resposta = confirm('Confirma a exclusão de todos os contatos? ')
	if(resposta === true){
		window.location.href = "deleteAll"
	}
}