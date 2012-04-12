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
            itemId:'dtFrom'
        },
        {
            xtype:'datefield',
            fieldLabel:'По',
            name:'to',
            format:'d.m.Y',
            validateOnChange:false,
            labelWidth:65,
            width:175,
            itemId:'dtTo'
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
            itemId:'cmbCat'
        },
        {
            xtype:'button',
            text:'Удалить',
            handler:function() {
                this.ownerCt.fireEvent('transdelete');
            }
        }
    ],

    dtFrom:null,
    dtTo:null,
    cmbCat:null,
    storeCat:null,

    initComponent:function() {
        var me = this;
        me.storeCat = Ext.create('alexzam.his.model.account.store.Category', {
            proxy:Ext.create('alexzam.his.model.account.proxy.Category', {
                rootUrl:me.rootUrl
            })
        });

        me.items[2].store = me.storeCat; // Not so good but seems most efficient

        me.callParent();

        me.dtFrom = me.getComponent('dtFrom');
        me.dtTo = me.getComponent('dtTo');
        me.cmbCat = me.getComponent('cmbCat');

        me.dtFrom.on('change', me.onFilterChange, me);
        me.dtTo.on('change', me.onFilterChange, me);
        me.cmbCat.on('change', me.onFilterChange, me);

        me.addEvents(['filterupdate', 'transdelete']);

        var d = new Date();
        d.setDate(1);
        me.dtFrom.setValue(d);
    },

    reloadCategories:function() {
        var me = this;
        var cmp = me.cmbCat;
        var val = cmp.getValue();
        cmp.setValue('');
        me.storeCat.load();
        cmp.setValue(val);
        cmp.getPicker().setLoading(false);
    },

    onFilterChange:function() {
        var me = this;
        me.fireEvent('filterupdate');

        me.dtTo.setMinValue(me.dtFrom.getValue());
        me.dtFrom.setMaxValue(me.dtTo.getValue());
    }
});