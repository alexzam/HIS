var account = {
    setAddFormFullValidation:function(enable) {
        var cmp = Ext.getCmp('tbAddDate');
        cmp.allowBlank = !enable;
        cmp.isValid();
        cmp = Ext.getCmp('tbAddAmount');
        cmp.allowBlank = !enable;
        cmp.isValid();
        cmp = Ext.getCmp('cbCategory');
        cmp.allowBlank = !enable;
        cmp.isValid();
    },

    resetAddForm:function() {
        Ext.getCmp('tbAddAmount').setValue(null);
        Ext.getCmp('cbCategory').setValue(null);
    },

    onFilterChange:function() {
        account.loadTransactions();

        var tbFrom = Ext.getCmp('tbFilterFrom');
        var tbTo = Ext.getCmp('tbFilterTo');
        tbTo.setMinValue(tbFrom.getValue());
        tbFrom.setMaxValue(tbTo.getValue());
    },

    onBtDelete: function() {
        var selected = Ext.getCmp('gridTrans').getSelectionModel().getSelection();
        var delIds = [];

        for (i in selected) {
            var sel = selected[i];
            delIds.push(sel.get('id'));
        }

        if (delIds.length <= 0) return;
        var idsStr = delIds.join();

        var data = {act:"del", ids:idsStr};
        Ext.Ajax.request({
            method:'POST',
            url:transStoreUrl,
            params:data,
            callback:function() {
                account.loadTransactions();
                account.loadCategories();
                account.updateAccountStats();
            }
        });
    }
};

Ext.onReady(function() {

    Ext.create('Ext.container.Viewport', srcScreen);

    var d = new Date();
    d.setDate(1);
    Ext.getCmp('tbFilterFrom').setValue(d);

    account.updateAccountStats();
});