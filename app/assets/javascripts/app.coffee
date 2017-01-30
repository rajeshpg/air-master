window.AM = {}
class AM.Main

  init: ->
    @amAutoComplete()

  amAutoComplete: ->
    $("#countryCode").autocomplete(
      source: (request, response) ->
        $.ajax
          url: "/countries"
          data:
            searchText: request.term
          success: (data) ->
            labelValueArray = new Array
            i = 0
            while i < data.length
              labelValue =
                label: data[i].name + "(" + data[i].code + ")"
                value: data[i].code
              labelValueArray.push labelValue
              i++
            response labelValueArray
            return
        return
      select: (event, ui) =>
        $("#countryCode").val ui.item.value
        $("#searchCountry").submit()
        return
      minLength: 2
    )


