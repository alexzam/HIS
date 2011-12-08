var account = {


};

Ext.onReady(function() {

    Ext.create('Ext.container.Viewport', srcScreen);

    var d = new Date();
    d.setDate(1);
    Ext.getCmp('tbFilterFrom').setValue(d);

    account.updateAccountStats();
});