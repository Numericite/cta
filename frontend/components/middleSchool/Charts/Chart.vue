
<script>

  import Chart from 'chart.js'
  import { generateChart, mixins } from 'vue-chartjs'

  const { reactiveProp } = mixins

  Chart.defaults.RoundedDoughnut    = Chart.helpers.clone(Chart.defaults.doughnut);
  Chart.controllers.RoundedDoughnut = Chart.controllers.doughnut.extend({
    draw: function(ease) {
      var ctx           = this.chart.ctx;
      var easingDecimal = ease || 1;
      var arcs          = this.getMeta().data;
      Chart.helpers.each(arcs, function(arc, i) {
        arc.transition(easingDecimal).draw();

        var pArc   = arcs[i === 0 ? arcs.length - 1 : i - 1];
        var pColor = pArc._view.backgroundColor;

        var vm         = arc._view;
        var radius     = (vm.outerRadius + vm.innerRadius) / 2;
        var thickness  = (vm.outerRadius - vm.innerRadius) / 2;
        var startAngle = Math.PI - vm.startAngle - Math.PI / 2;
        var angle      = Math.PI - vm.endAngle - Math.PI / 2;

        ctx.save();
        ctx.translate(vm.x, vm.y);

        ctx.fillStyle = i === 0 ? vm.backgroundColor : pColor;
        ctx.beginPath();
        ctx.arc(radius * Math.sin(startAngle), radius * Math.cos(startAngle), thickness, 0, 2 * Math.PI);
        ctx.fill();

        ctx.fillStyle = vm.backgroundColor;
        ctx.beginPath();
        ctx.arc(radius * Math.sin(angle), radius * Math.cos(angle), thickness, 0, 2 * Math.PI);
        ctx.fill();

        ctx.restore();
      });
    }
  });

  const RoundedDoughnut = generateChart('custom-rounded-doughnut', 'RoundedDoughnut')

  export default {
    extends: RoundedDoughnut,
    mixins: [reactiveProp],
    props: {
      options: {
        type: Object,
        default: null
      }
    },
    mounted () {
      this.renderChart(this.chartData, this.options)
    }
  }
</script>
