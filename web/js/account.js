var transStore;

var account = {
    loadTransactions:function() {
        transStore.close();
        transStore.fetch({
            onComplete:function() {
                dijit.byId('tabTrans').setStore(transStore);
            }
        });
    }
};

dojo.addOnLoad(function() {
    transStore = new dojo.data.ItemFileReadStore({
        url: fileStoreUrl,
        clearOnClose: true,
        urlPreventCache: true
    });

    account.loadTransactions();
});