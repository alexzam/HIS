var transStore;
var catStore;

var account = {
    loadTransactions:function() {
        transStore.close();

        var frm = dijit.byId('frmFilter');
        if (!frm.validate()) return;

        var q = frm.getValues();
        if (q.from != null) {
            q.from = q.from.getTime();
        }
        if (q.to != null) {
            q.to = q.to.getTime();
        }

        transStore.url = transStoreUrl + '?' + dojo.objectToQuery(q);
        console.dir(transStore.url);
        transStore.fetch({
            onComplete:function() {
                // When transaction store is updated
                dijit.byId('tabTrans').setStore(transStore);
            }
        });
    },

    loadCategories:function(firstTime) {
        catStore.close();
        catStore.url = catStoreUrl;
        catStore.fetch({
            onComplete:function() {
                var cbCat = dijit.byId('cbCategory');
                cbCat.store = catStore;
                cbCat.query = {"type":'e'};
                var selFilterCat = dijit.byId('filter_category');
                if (firstTime) {
                    selFilterCat.setStore(catStore, 0);
                    account.loadTransactions();
                } else {
                    selFilterCat.setStore(catStore);
                }
            }
        });
    },

    addSubmit:function() {
        var frm = dijit.byId('frmAddTrans');
        if (!frm.validate()) return;

        var data = frm.getValues();
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
        data.act = 'put';

        dojo.xhrPost({
            content:data,
            url:transStoreUrl,
            load:function() {
                account.loadTransactions();
                account.loadCategories(false);
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
    },

    onFilterChange:function() {
        account.loadTransactions();
    }
};

dojo.addOnLoad(function() {
    transStore = new dojo.data.ItemFileWriteStore({
        url: transStoreUrl,
        clearOnClose: true,
        urlPreventCache: true
    });

    catStore = new dojo.data.ItemFileReadStore({
        clearOnClose: true,
        urlPreventCache: true
    });

    account.loadCategories(true);

    dijit.byId('cbCategory').query = {"type":'e'};
});