Ext.define('alexzam.his.view.account.FilterForm', {
    extend:'Ext.form.Panel',
    alias:'widget.his.account.FilterForm',

    title:'Фильтр',
    layout:'vbox',
    bodyPadding:5,
    autoScroll:true,
    items:[
        {
            xtype:'datefield',
            fieldLabel:'С',
            name:'from',
            format:'d.m.Y',
            validateOnChange:false,
            labelWidth:65,
            width:175,
            itemId:'dtFrom',
            listeners:{
//                change:account.onFilterChange
            }
        },
        {
            xtype:'datefield',
            fieldLabel:'По',
            name:'to',
            format:'d.m.Y',
            validateOnChange:false,
            labelWidth:65,
            width:175,
            itemId:'dtTo',
            listeners:{
//                change:account.onFilterChange
            }
        },
        {
            xtype:'combo',
            fieldLabel:'Категория',
            name:'cat',
            queryMode:'local',
            valueField:'id',
            displayField:'name',
            lastQuery:'',
            labelWidth:65,
            width:175,
            itemId:'cmbCat',
            listeners:{
//                change:account.onFilterChange
            }
        },
        {
            xtype:'button',
            text:'Удалить'
//            ,
//            handler:account.onBtDelete
        }
    ],

    dtFrom:null,
    dtTo:null,
    cmbCat:null,

    initComponent:function() {
        var me = this;
        var store = Ext.create('alexzam.his.model.account.store.Category', {
            storeId:'stFilterCats',
            proxy:Ext.create('alexzam.his.model.account.proxy.Category', {
                rootUrl:me.rootUrl
            })
        });

        me.items[2].store = store; // Not so good but seems most efficient

        me.callParent();

        me.dtFrom = me.getComponent('dtFrom');
        me.dtTo = me.getComponent('dtTo');
        me.cmbCat = me.getComponent('cmbCat');
    }
});