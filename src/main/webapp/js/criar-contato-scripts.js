$(function(){
    $('#botaoCriarContato').click(function(){

        // captura os dados dos campos
        let nome = $('#nome').val();
        let fone = $('#fone').val();
        let email = $('#email').val();

        // verifica se os campos que são obrigatórios estão vazios
        if(nome == "" || fone == "") {
            alert('Preencha os campos obrigatórios!');
        }
        else {
            // cria novo contato por POST
            $.post(`http://localhost:8080/agenda/controller`, {prm: 'criar', nom: nome, fon: fone, mail: email}, function(status){});
            location.href="../pag/main.html";
        }
    });

    // volta para o menu principal
    $('#botaoVoltarMain').click(function(){
        location.href="../pag/main.html";
    });
});