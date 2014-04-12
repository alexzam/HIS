Ext.define('alexzam.his.view.account.AccountChooser', {
    extend:'Ext.form.Panel',
    xtype:'az-accChooser',

    requires:[
        'Ext.form.Label'
    ],

    layout:'hbox',
    bodyPadding:5,

    combo: null,

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

        me.combo = me.getComponent('cbAccount');

        me.combo.select(me.combo.getStore().first());
        me.combo.on('change', me.onChange, me);

        me.addEvents(['filterupdate']);
    },

    getValue:function(){
        return this.combo.getValue();
    },

    onChange:function(){
        this.fireEvent('filterupdate');
    }
});