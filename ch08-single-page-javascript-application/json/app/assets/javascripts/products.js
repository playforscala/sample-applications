// Load and append product details to a table row
function loadProductDetails(tableRow) {
    var eanCode = tableRow.text()
    $.ajax({
        method:"GET",
        url:"/products/"+eanCode
    }).done((product) => {
        tableRow.append($('<td/>').text(product.name))
        tableRow.append($('<td/>').text(product.description))
        tableRow.append($('<td/>'))
    })
}

// Save an edited table row to the server
function saveRow($row) {
    var cells = $row.children().map(function() {
        return $(this).text()
    }).get()
    product = {
        ean: parseInt(cells[0]),
        name: cells[1],
        description: cells[2]
    }
    $.ajax({
        type: "PUT",
        url: "/products/"+product.ean,
        contentType: "application/json",
        data: JSON.stringify(product)
    }).done((response) => {
        $label = $('<span/>').addClass('label label-success')
        $row.children().last().append($label.text(response))
        $label.delay(3000).fadeOut()
    }).fail((data) => {
        $label = $('<span/>').addClass('label label-important')
        message = data.responseText || data.statusText
        $row.children().last().append($label.text(message))
    })
}

$(function() { // make sure code runs after page load
    var $table = $('#products_table')

    // Attach edit handling to editable elements
    $table.on('focusout', 'tr', function() {
        saveRow($(this))
    })

    $.ajax({
        method:"GET",
        url:"/products"
    }).done(function(data) {
        $.each(data, function(index, eanCode) {
            var row = $('<tr/>').append($('<td/>').text(eanCode))
            row.attr('contenteditable', true)
            $table.append(row)
            loadProductDetails(row)
        })
    });
});
