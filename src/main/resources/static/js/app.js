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

function addItemToCart(tmdbId)
{
    $.ajax ({
        url: '/cart/items',
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data : '{"tmdbId":"'+ tmdbId +'"}"',
        complete: function(responseData, status, xhttp){
            updateCartItemCount();
        }
    });
}

function updateCartItemQuantity(tmdbId, quantity)
{
    $.ajax ({
        url: '/cart/items',
        type: "PUT",
        dataType: "json",
        contentType: "application/json",
        data : '{ "product" :{ "tmdbId":"'+ tmdbId +'"},"quantity":"'+quantity+'"}',
        complete: function(responseData, status, xhttp){
            updateCartItemCount();
            location.href = '/cart'
        }
    });
}

function removeItemFromCart(tmdbId) {
    $.ajax ({
        url: '/cart/items/'+tmdbId,
        type: "DELETE",
        dataType: "json",
        contentType: "application/json",
        complete: function(responseData, status, xhttp){
            updateCartItemCount();
            location.href = '/cart'
        }
    });
}
