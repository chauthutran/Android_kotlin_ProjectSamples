
export default function ClientIcon({clientFullName}) {
    
    const getIconText = (text: string) => {
        return text.split(' ')
        .map((word: string) => word[0])
        .join('').substring(0,2).toUpperCase();
    };

    
    // ==============================================================================
    // For COLOR

    const convertStringToLightColour = function(str: string): string {
        var hash = 0;
        for (var i = 0; i < str.length; i++) {
            hash = str.charCodeAt(i) + ((hash << 5) - hash);
        }
        var colour = '#';
        for (var i = 0; i < 3; i++) {
            var value = (hash >> (i * 8)) & 0xFF;
            colour += ('00' + value.toString(16)).substr(-2);
        }
        return colour;
    }

    const getContrastingColor = function(hexColor: string): string {
        const r = parseInt(hexColor.slice(1, 3), 16);
        const g = parseInt(hexColor.slice(3, 5), 16);
        const b = parseInt(hexColor.slice(5, 7), 16);

        const luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;

        return luminance > 0.5 ? '#000000' : '#FFFFFF';
    }

    const bgColor = convertStringToLightColour(clientFullName);
    const textColor = getContrastingColor(bgColor);

    return (
        <div className="flex flex-row items-center justify-center w-14 h-14 rounded-full text-xl font-bold" style={{backgroundColor: bgColor, color: textColor}}>
            {getIconText(clientFullName)}
        </div>
    )
}