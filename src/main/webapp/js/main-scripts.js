$(function(){
    $('#dadosTabela').html('<img class="mt-4" src="../img/circular.gif" width="50">');
    
    // quando a página for carregada, ela envia uma requisição por GET, solicitando a listagem de todos os contatos do Banco
    $.get(`http://localhost:8080/agenda/controller?prm=listar`, function(dados, status){
        if(dados != null && status == 'success') {
            let codigoTabela = "";
            for(var i=0; i<dados.length; i++) {
                codigoTabela += `<tr class="text-center border-bottom border-secondary">
                            <td>${dados[i].id}</td>
                            <td>${dados[i].nome}</td>
                            <td>${dados[i].fone}</td>
                            <td>${dados[i].email}</td>
                            <td>
                                <button type="button" class="btn btn-primary" title="Editar Contato" onclick='editarContato(${dados[i].id})'>Editar</button>
                                <button type="button" class="btn btn-danger" title="Apagar Contato" onclick='excluirContato(${dados[i].id})'>Excluir</button>
                            </td>
                        </tr>`
            }
            $('#dadosTabela').html(codigoTabela);
        }
        else if(dados == undefined && status == 'success') {
            $('#dadosTabela').html('<h1>Base de Dados está fora de Operação!</h1>');
        }
        else {
            $('#dadosTabela').html('<h1>Cadastro está vazio!</h1>');
        }
    });

    // chama a página que contém o formulário para criar um novo contato
    $('#botaoNovoContato').click(function() {
        location.href="../pag/criar-contato.html";
    });
})

// chama a página contendo o contato escolhido para atulizar os dados, enviando o id por parâmetro contida na url
function editarContato(id) {
    location.href=`../pag/editar-contato.html?id=${id}`;
}

// opção que apaga contato do Banco de Dados, recebendo o id clicado no botão Excluir
function excluirContato(id) {
    let resposta = confirm(`Tem certeza que deseja excluir esse contato?`);
    if(resposta) {
        $.post(`http://localhost:8080/agenda/controller`, {prm: 'apagar', id: id}, function(status){});
        location.href="../pag/main.html";
    }
}