
function criteriaChanged() {

    if( filterCriteria.value == "BETWEEN" ) {
                                filterValue2.style.display = "";
    } else {

        filterValue2.style.display = "none";
        filterValue2.value ="";
        
    }

}

function filterInitialized() {
    $('filterError').update('');
    $('filterBusy').style.display = "";
}

function filterFinished() {
    $('filterBusy').style.display = "none";
}