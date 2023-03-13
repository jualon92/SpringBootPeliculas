function actorSelected(selected) {
  let index = selected.selectedIndex; // obtener id de seleccion
  let option = selected.options[index]; //tomar de opciones el que tenga ese id
  let id = option.value;
  let nombre = option.text;
  let urlImagen = option.dataset.url;

  option.disabled = "disabled"; //no volver a habilitarlo, ya fue elegido
  selected.selectedIndex = 0; //volver a posicion 0

  $("#protagonistas_container").append(getActorHTML(id, nombre, urlImagen));

  let ids = $("#ids").val();

  if (ids === "") {
    $("#ids").val(id);
  } else {
    //ya existe existe id, concatenarla
    $("#ids").val(ids + "," + id);
  }
}

function getActorHTML(id, nombre, urlImagen) {
  return  `
    <div class="card col-md-3 m-2 " style="width: 10rem">
    <img src="${urlImagen}" alt="" class="card-img-top">
    <div class="card-body">
        <p class="card-text">${nombre}</p>
        <button class="btn btn-danger" data-id="${id}" onclick="eliminarActor(this); return false;">Eliminar</button>
    </div>
</div>
    `;
 
}

function eliminarActor(btn){
    let id= btn.dataset.id;
    console.log("id", id);
    let node = btn.parentElement.parentElement;
    let arrayIds = $("#ids").val().split(",").filter(idActor => idActor != id) //quitar filtro

    $("#ids").val(arrayIds.join(","));

    //volver a habilitar opcion
    $("#protagonistas option[value='" + id + "']").prop("disabled", false);

    $(node).remove();
}


function previsualizar(){
    let reader = new FileReader();
    reader.readAsDataURL(document.getElementById("imagen").files[0])

    reader.onload = function(e){ //al cargar, volver visible
        let vista = document.getElementById("vista_previa")
        vista.classList.remove("d-none")
        vista.style.backgroundImage = 'url("' + e.target.result +  '")'
    }

}