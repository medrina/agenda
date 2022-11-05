$(function(){

    // captura a url e localiza o parâmetro que veio junto (id)
    let url = window.location.href;
    let res = url.split('=');
    let param = res[1];
    
    // realiza a consulta e retorna um único contato lá do Banco por GET através do id capturado 
    $.get(`http://localhost:8080/agenda/controller?prm=unico&id=${param}`, function(dados, status){
        if(dados != null) {
            $('#id').val(`${dados[0].id}`);
            $('#nome').val(`${dados[0].nome}`);
            $('#fone').val(`${dados[0].fone}`);
            $('#email').val(`${dados[0].email}`);
        }
        else {
            $('#dadosTabela').html('<h1>ERRO</h1>');
        }
    });

    // volta para menu principal
    $('#botaoVoltarMain').click(function(){
        location.href="../pag/main.html";
    })

    // envia os dados para atualizar/editar o contato já existente lá no Banco por POST
    $('#botaoSalvar').click(function(){
        let id = $('#id').val();
        let nome = $('#nome').val();
        let fone = $('#fone').val();
        let email = $('#email').val();
        
        if(nome == "" || fone == "") {
            alert('Preencha os campos obrigatórios!');
        }
        else {
            $.post(`http://localhost:8080/agenda/controller`, {prm: 'editar', id: id, nom: nome, fon: fone, mail: email}, function(status){});
            location.href="../pag/main.html";
        }
    });

});