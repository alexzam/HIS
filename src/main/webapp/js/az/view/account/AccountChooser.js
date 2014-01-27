Ext.define('alexzam.his.view.account.AccountChooser', {
    extend:'Ext.form.Panel',
    xtype:'az-accChooser',

    requires:[
        'Ext.form.Label'
    ],

    layout:'hbox',
    bodyPadding:5,

    items:[
        {
            xtype:'label',
            text:'Счёт:',
            style:{
                fontWeight:'bold'
            }
        },
        {
            xtype: 'combo',
            name: 'account',
            itemId: 'cbAccount',
            queryMode: 'local',
            valueField: 'id',
            displayField: 'name',
            store: 'accounts',
            allowBlank: false,
            forceSelection: true,
            autoSelect: true
        }
    ],

    initComponent:function () {
        var me = this;
        me.callParent();

        var combo = me.getComponent('cbAccount');

        combo.select(combo.getStore().first());
    }
});