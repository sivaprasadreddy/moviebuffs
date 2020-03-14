jQuery(document).ready(function($){
    updateCartItemCount();
});

function updateCartItemCount()
{
    $.ajax ({
        url: '/cart/items/count',
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        complete: function(responseData, status, xhttp){
            $('#cart-item-count').text('('+responseData.responseJSON.count+')');
        }
    });
}

function addItemToCart(id)
{
    $.ajax ({
        url: '/cart/items',
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data : '{"id":"'+ id +'"}"',
        complete: function(responseData, status, xhttp){
            updateCartItemCount();
        }
    });
}

function updateCartItemQuantity(id, quantity)
{
    $.ajax ({
        url: '/cart/items',
        type: "PUT",
        dataType: "json",
        contentType: "application/json",
        data : '{ "product" :{ "id":"'+ id +'"},"quantity":"'+quantity+'"}',
        complete: function(responseData, status, xhttp){
            updateCartItemCount();
            location.href = '/cart'
        }
    });
}

function removeItemFromCart(id) {
    $.ajax ({
        url: '/cart/items/'+id,
        type: "DELETE",
        dataType: "json",
        contentType: "application/json",
        complete: function(responseData, status, xhttp){
            updateCartItemCount();
            location.href = '/cart'
        }
    });
}
