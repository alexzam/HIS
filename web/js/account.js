var transStore;
var catStore;

var account = {
    loadTransactions:function() {
        transStore.close();
        transStore.fetch({
            onComplete:function() {
                dijit.byId('tabTrans').setStore(transStore);
            }
        });
    },

    loadCategories:function() {
        catStore.close();
        catStore.url = catStoreUrl + (dijit.byId('rbTypeExp').checked ? 'e' : 'i');
        catStore.fetch({
            onComplete:function() {
                dijit.byId('cbCategory').store = catStore;
            }
        });
    },

    addSubmit:function() {
        var frm = dijit.byId('frmAddTrans');
        if (!frm.validate()) return;

        var data = frm.getValues();
        if (data.type == 'e') data.amount = -data.amount;
        data.date = data.date.getTime();

        var cbcat = dijit.byId('cbCategory');
        if (cbcat.item == null) {
            // New category
            data.catname = data.cat;
            data.cat = 0;
        } else {
            data.cat = cbcat.item.id[0];
        }

        if (data.actor == 0) data.actor = uid;
        data.act = 'addtr';

        dojo.xhrPost({
            content:data,
            url:transStoreUrl,
            load:function() {
                account.loadTransactions();
                account.loadCategories();
                account.updateAccountAmount();

                dijit.byId('taAddComment').set('value', '');
                dijit.byId('cbCategory').set('value', '');
                dijit.byId('tbAddAmount').set('value', '');
            }
        });
    },

    updateAccountAmount:function() {
        dojo.xhrGet({
            url:transStoreUrl + '?act=getamount',
            handleAs:'json',
            load:function(data) {
                dojo.byId('account_amount').innerHTML = data.amount + ' р.';
            }
        });
    },

    onTableColResize:function() {
        // TODO Make last column 100% width
    }
};

dojo.addOnLoad(function() {
    transStore = new dojo.data.ItemFileReadStore({
        url: transStoreUrl,
        clearOnClose: true,
        urlPreventCache: true
    });

    catStore = new dojo.data.ItemFileReadStore({
        clearOnClose: true,
        urlPreventCache: true
    });

    account.loadTransactions();
    account.loadCategories();

    dijit.byId('rbTypeExp').onChange = function() {
        account.loadCategories();
    };
});