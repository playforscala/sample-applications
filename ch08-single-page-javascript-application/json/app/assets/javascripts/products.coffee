jQuery ($) ->

  # DOM element and DOM meta-data.
  $table = $('.container table')
  productListUrl = $table.data('list')

  # Load one column of EAN codes
  loadProductTable = ->
    $.get productListUrl, (products) ->
      $.each products, (index, eanCode) ->
        row  = $('<tr/>').append $('<td/>').text(eanCode)
        row.attr 'contenteditable', true
        $table.append row
        loadProductDetails row

  # Construct a URL by replacing the EAN code parameter
  productDetailsUrl = (eanCode) ->
    $table.data('details').replace '0', eanCode

  # Load and append product details to a table row.
  loadProductDetails = (tableRow) ->
    eanCode = tableRow.text()
    $.get productDetailsUrl(eanCode), (product) ->
      tableRow.append $('<td/>').text(product.name)
      tableRow.append $('<td/>').text(product.description)
      tableRow.append $('<td/>')

  loadProductTable()


  # Save an edited table row to the server
  saveRow = ($row) ->
    [ean, name, description] = $row.children().map -> $(this).text()
    product =
      ean: parseInt(ean)
      name: name
      description: description
    jqxhr = $.ajax
      type: "PUT"
      url: productDetailsUrl(ean)
      contentType: "application/json"
      data: JSON.stringify product
    jqxhr.done (response) ->
      $label = $('<span/>').addClass('label label-success')
      $row.children().last().append $label.text(response)
      $label.delay(3000).fadeOut()
    jqxhr.fail (data) ->
      $label = $('<span/>').addClass('label label-important')
      message = data.responseText || data.statusText
      $row.children().last().append $label.text(message)

  # Attach edit handling to editable elements.
  $table.on 'focusout', 'tr', () ->
    console.log('save')
    saveRow $(this)
